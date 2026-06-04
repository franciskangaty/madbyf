package dev.madbyf.authorization.user.api;

import dev.madbyf.authorization.user.api.dto.ContactDTO;
import dev.madbyf.authorization.user.api.dto.UserRegistrationRequest;
import dev.madbyf.authorization.user.api.dto.UserSearchCriteria;
import dev.madbyf.authorization.user.api.dto.UserUpdateRequest;
import dev.madbyf.authorization.user.api.dto.UserWithContacts;
import dev.madbyf.authorization.user.domain.model.ContactType;
import dev.madbyf.authorization.user.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRegistrationRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(
            @PathVariable UUID id,
            @RequestBody UserUpdateRequest request
    ) {
        userService.updateUser(id, request);
    }

    @PutMapping("/{userId}/contacts/{contactId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateContact(
            @PathVariable UUID userId,
            @PathVariable UUID contactId,
            @Valid @RequestBody ContactDTO request
    ) {
        userService.updateContact(userId, contactId, request);
    }

    @PatchMapping("/{id}/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyUser(@PathVariable UUID id) {
        userService.verifyUser(id);
    }

    @PatchMapping("/{userId}/contacts/{contactId}/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyContact(
            @PathVariable UUID userId,
            @PathVariable UUID contactId
    ) {
        userService.verifyContact(userId, contactId);
    }

    @PatchMapping("/{id}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableUser(@PathVariable UUID id) {
        userService.enableUser(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public UserWithContacts getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @GetMapping
    public Page<UserWithContacts> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) Boolean verified,
            @RequestParam(required = false) ContactType contactType,
            @RequestParam(required = false) String contactValue,
            @PageableDefault(size = 20, sort = "username") Pageable pageable
    ) {
        var criteria = new UserSearchCriteria(
                username,
                firstName,
                lastName,
                role,
                enabled,
                verified,
                contactType,
                contactValue
        );
        return userService.getUsers(criteria, pageable);
    }
}
