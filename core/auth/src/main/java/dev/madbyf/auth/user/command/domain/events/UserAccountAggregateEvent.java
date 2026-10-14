package dev.madbyf.auth.user.command.domain.events;

import java.time.LocalDate;
import java.util.UUID;

public final class UserAccountAggregateEvent {

    private UserAccountAggregateEvent() {}

    // ---- CRUD ----

    public record Created(
            UUID userId,
            String firstName,
            String middleName,
            String lastName,
            LocalDate dateOfBirth,
            String passwordHash
    ) {}

    public record NamesUpdated(
            UUID userId,
            String oldFirstName,
            String oldMiddleName,
            String oldLastName,
            String newFirstName,
            String newMiddleName,
            String newLastName
    ) {}

    public record DateOfBirthUpdated(
            UUID userId,
            LocalDate oldDateOfBirth,
            LocalDate newDateOfBirth
    ) {}

    public record PasswordUpdated(
            UUID userId,
                String newPasswordHash
    ) {}

    public record ProfilePhotoUrlSet(
            UUID userId,
            String newProfilePhotoUrl
    ) {}

    // ---- Verification ----

    public record NameVerified(
            UUID userId,
            String verificationMethod
    ) {}

    public record AgeVerified(
            UUID userId,
            String verificationMethod
    ) {}

    // ---- Security principal state ----

    public record PasswordSet(
            UUID userId
    ) {}

    public record PasswordRemoved(
            UUID userId
    ) {}

    public record Enabled(
            UUID userId
    ) {}

    public record Disabled(
            UUID userId,
            String reason
    ) {}

    public record Locked(
            UUID userId,
            String reason
    ) {}

    public record Unlocked(
            UUID userId
    ) {}

    public record CredentialsExpired(
            UUID userId
    ) {}

    public record CredentialsRenewed(
            UUID userId
    ) {}

    // ---- Account lifecycle ----

    public record AccountExpired(
            UUID userId
    ) {}

    public record AccountReactivated(
            UUID userId
    ) {}
}
