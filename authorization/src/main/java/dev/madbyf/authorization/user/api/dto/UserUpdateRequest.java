package dev.madbyf.authorization.user.api.dto;

import java.util.Set;

public record UserUpdateRequest(
        String username,
        String firstName,
        String middleName,
        String lastName,
        String password,
        Set<String> roles
) {
}
