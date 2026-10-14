package dev.madbyf.authorization.user.api.dto;

import dev.madbyf.authorization.user.domain.model.ContactType;

import java.util.UUID;

public record ContactResponse(
        UUID id,
        ContactType type,
        String value,
        boolean verified
) {
}
