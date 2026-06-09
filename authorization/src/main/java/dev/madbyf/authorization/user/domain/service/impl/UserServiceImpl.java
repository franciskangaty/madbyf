package dev.madbyf.authorization.user.domain.service.impl;

import dev.madbyf.authorization.user.api.dto.ContactDTO;
import dev.madbyf.authorization.user.api.dto.ContactResponse;
import dev.madbyf.authorization.user.api.dto.UserRegistrationRequest;
import dev.madbyf.authorization.user.api.dto.UserSearchCriteria;
import dev.madbyf.authorization.user.api.dto.UserUpdateRequest;
import dev.madbyf.authorization.user.api.dto.UserWithContacts;
import dev.madbyf.authorization.user.domain.model.Contact;
import dev.madbyf.authorization.user.domain.model.User;
import dev.madbyf.authorization.user.domain.repository.ContactRepository;
import dev.madbyf.authorization.user.domain.repository.UserRepository;
import dev.madbyf.authorization.user.domain.repository.utils.UserSpecifications;
import dev.madbyf.authorization.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(UserRegistrationRequest request) {
        var username = requireNotBlank(request.username(), "username");
        ensureUsernameAvailable(username, null);
        ensureContactsAvailable(request.contacts());

        var user = new User();
        user.setUsername(username);
        user.setFirstName(trimToNull(request.firstName()));
        user.setMiddleName(trimToNull(request.middleName()));
        user.setLastName(trimToNull(request.lastName()));
        user.setPassword(passwordEncoder.encode(requireNotBlank(request.password(), "password")));
        user.setRoles(normalizeRoles(request.roles()));
        user.setEnabled(false);
        user.setVerified(false);

        var savedUser = userRepository.save(user);
        if (request.contacts() != null && !request.contacts().isEmpty()) {
            contactRepository.saveAll(request.contacts().stream()
                    .map(contact -> toContact(contact, savedUser))
                    .toList());
        }
    }

    @Override
    public void updateUser(UUID id, UserUpdateRequest request) {
        var user = findUser(id);

        if (request.username() != null) {
            var username = requireNotBlank(request.username(), "username");
            ensureUsernameAvailable(username, id);
            user.setUsername(username);
        }
        if (request.firstName() != null) {
            user.setFirstName(trimToNull(request.firstName()));
        }
        if (request.middleName() != null) {
            user.setMiddleName(trimToNull(request.middleName()));
        }
        if (request.lastName() != null) {
            user.setLastName(trimToNull(request.lastName()));
        }
        if (request.password() != null) {
            user.setPassword(passwordEncoder.encode(requireNotBlank(request.password(), "password")));
        }
        if (request.roles() != null) {
            user.setRoles(normalizeRoles(request.roles()));
        }
    }

    @Override
    public void updateContact(UUID userId, UUID contactId, ContactDTO request) {
        var contact = findContact(userId, contactId);
        var newValue = requireNotBlank(request.value(), "contact value");
        var newType = request.type();
        if (newType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contact type is required");
        }
        if (contactRepository.existsByValueAndIdNot(newValue, contactId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "contact value is already in use");
        }

        var changed = !contact.getValue().equals(newValue) || contact.getType() != newType;
        contact.setType(newType);
        contact.setValue(newValue);
        if (changed) {
            contact.setVerified(false);
        }
    }

    @Override
    public void verifyUser(UUID id) {
        findUser(id).setVerified(true);
    }

    @Override
    public void verifyContact(UUID userId, UUID contactId) {
        findContact(userId, contactId).setVerified(true);
    }

    @Override
    public void enableUser(UUID id) {
        findUser(id).setEnabled(true);
    }

    @Override
    public void deleteUser(UUID id) {
        var user = findUser(id);
        contactRepository.deleteAll(contactRepository.findByUser_Id(id));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserWithContacts getUser(UUID id) {
        var user = findUser(id);
        return toUserWithContacts(user, contactRepository.findByUser_Id(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserWithContacts> getUsers(UserSearchCriteria criteria, Pageable pageable) {
        var users = userRepository.findAll(
            UserSpecifications.search(criteria),
            pageable
        );
        // var users = userRepository.search(
        //         trimToNull(criteria.username()),
        //         trimToNull(criteria.firstName()),
        //         trimToNull(criteria.lastName()),
        //         normalizeRoleOrNull(criteria.role()),
        //         criteria.enabled(),
        //         criteria.verified(),
        //         criteria.contactType(),
        //         trimToNull(criteria.contactValue()),
        //         pageable
        // );
        var contactsByUserId = contactsByUserId(users.getContent());

        return users.map(user -> toUserWithContacts(
                user,
                contactsByUserId.getOrDefault(user.getId(), List.of())
        ));
    }

    // ======================================= UTILS ====================================================

    private User findUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    private Contact findContact(UUID userId, UUID contactId) {
        return contactRepository.findByIdAndUser_Id(contactId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));
    }

    private void ensureUsernameAvailable(String username, UUID currentUserId) {
        userRepository.findByUsernameOrContactValue(username).ifPresent(existing -> {
            if (!existing.getId().equals(currentUserId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "username is already in use");
            }
        });
    }

    private void ensureContactsAvailable(List<ContactDTO> contacts) {
        if (contacts == null || contacts.isEmpty()) {
            return;
        }

        Set<String> values = new HashSet<>();
        for (var contact : contacts) {
            if (contact.type() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contact type is required");
            }
            var value = requireNotBlank(contact.value(), "contact value");
            if (!values.add(value.toLowerCase(Locale.ROOT))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "duplicate contact value in request");
            }
            if (contactRepository.existsByValue(value)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "contact value is already in use");
            }
        }
    }

    private Contact toContact(ContactDTO request, User user) {
        var contact = new Contact();
        contact.setType(request.type());
        contact.setValue(requireNotBlank(request.value(), "contact value"));
        contact.setUser(user);
        contact.setVerified(false);
        return contact;
    }

    private UserWithContacts toUserWithContacts(User user, List<Contact> contacts) {
        return new UserWithContacts(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                copyRoles(user.getRoles()),
                user.isEnabled(),
                user.isVerified(),
                contacts.stream().map(this::toContactResponse).toList(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private ContactResponse toContactResponse(Contact contact) {
        return new ContactResponse(
                contact.getId(),
                contact.getType(),
                contact.getValue(),
                contact.isVerified()
        );
    }

    private Map<UUID, List<Contact>> contactsByUserId(List<User> users) {
        if (users.isEmpty()) {
            return Map.of();
        }

        var userIds = users.stream().map(User::getId).toList();
        return contactRepository.findByUser_IdIn(userIds).stream()
                .collect(Collectors.groupingBy(contact -> contact.getUser().getId()));
    }

    private Set<String> normalizeRoles(Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return new LinkedHashSet<>(Set.of("USER"));
        }

        var normalizedRoles = roles.stream()
                .map(this::normalizeRoleOrNull)
                .filter(role -> role != null)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (normalizedRoles.isEmpty()) {
            return new LinkedHashSet<>(Set.of("USER"));
        }
        return normalizedRoles;
    }

    private String normalizeRoleOrNull(String role) {
        var normalizedRole = trimToNull(role);
        if (normalizedRole == null) {
            return null;
        }

        normalizedRole = normalizedRole.toUpperCase(Locale.ROOT);
        if (normalizedRole.startsWith("ROLE_")) {
            return normalizedRole.substring("ROLE_".length());
        }
        return normalizedRole;
    }

    private Set<String> copyRoles(Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return Set.of();
        }
        return Set.copyOf(roles);
    }

    private String requireNotBlank(String value, String field) {
        var trimmed = trimToNull(value);
        if (trimmed == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " is required");
        }
        return trimmed;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        var trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
