package dev.madbyf.authorization.user.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;

public record UserRegistrationRequest(
        @NotBlank
        String username,
        @NotBlank
        String firstName,
        String middleName,
        String lastName,
        @NotBlank
        String password,
        Set<String> roles,
        List<@Valid ContactDTO> contacts
) {
}
