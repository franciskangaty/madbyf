package dev.madbyf.authorization.user.api.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String firstName,
        String middleName,
        String lastName,
        Set<String> roles,
        boolean enabled,
        boolean verified,
        Instant createdAt,
        Instant updatedAt
) {
}
