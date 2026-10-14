package dev.madbyf.authorization.user.api.dto;

import dev.madbyf.authorization.user.domain.model.ContactType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContactDTO(
        @NotNull
        ContactType type,
        @NotBlank
        String value
) {
}
