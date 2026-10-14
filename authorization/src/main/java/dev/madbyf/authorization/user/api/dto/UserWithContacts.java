package dev.madbyf.authorization.user.api.dto;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record UserWithContacts(
        UUID id,
        String username,
        String firstName,
        String middleName,
        String lastName,
        Set<String> roles,
        boolean enabled,
        boolean verified,
        List<ContactResponse> contacts,
        Instant createdAt,
        Instant updatedAt
) {
}
