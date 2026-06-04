package dev.madbyf.authorization.user.domain.service;

import dev.madbyf.authorization.user.api.dto.ContactDTO;
import dev.madbyf.authorization.user.api.dto.UserRegistrationRequest;
import dev.madbyf.authorization.user.api.dto.UserSearchCriteria;
import dev.madbyf.authorization.user.api.dto.UserUpdateRequest;
import dev.madbyf.authorization.user.api.dto.UserWithContacts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    void register(UserRegistrationRequest request);

    void updateUser(UUID id, UserUpdateRequest request);

    void updateContact(UUID userId, UUID contactId, ContactDTO request);

    void verifyUser(UUID id);

    void verifyContact(UUID userId, UUID contactId);

    void enableUser(UUID id);

    void deleteUser(UUID id);

    UserWithContacts getUser(UUID id);

    Page<UserWithContacts> getUsers(UserSearchCriteria criteria, Pageable pageable);
}
