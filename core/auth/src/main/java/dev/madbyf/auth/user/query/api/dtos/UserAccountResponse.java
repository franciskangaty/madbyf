package dev.madbyf.auth.user.query.api.dtos;

import java.time.LocalDate;
import java.util.UUID;

public record UserAccountResponse(
        UUID userId,
        String firstName,
        String middleName,
        String lastName,
        LocalDate dateOfBirth,
        String profilePhotoUrl,
        boolean nameVerified,
        boolean ageVerified,
        boolean passwordSet,
        boolean active,
        boolean suspended,
        boolean credentialExpired,
        boolean accountExpired,
        boolean locked
) {
}
