package dev.madbyf.authorization.user.api.dto;

import dev.madbyf.authorization.user.domain.model.ContactType;

public record UserSearchCriteria(
        String username,
        String firstName,
        String lastName,
        String role,
        Boolean enabled,
        Boolean verified,
        ContactType contactType,
        String contactValue
) {
}
