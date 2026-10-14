package dev.madbyf.auth.user.command.domain.events;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public final class UserAccountEvent {

    // =========================
    // CRUD
    // =========================

    public record Registered(
        UUID registrationId,
        String firstName,
        String middleName,
        String lastName,
        String username,
        Map<String, String> contacts,
        LocalDate dateOfBirth,
        String passwordHash
    ) {
    }

    public record Added(
        UUID userAccountId,
        String firstName,
        String middleName,
        String lastName,
        LocalDate dateOfBirth,
        String passwordHash
    ) {
    }

    public record InfoModified(
        UUID userAccountId,

        String oldFirstName,
        String newFirstName,

        String oldMiddleName,
        String newMiddleName,

        String oldLastName,
        String newLastName,

        LocalDate oldDateOfBirth,
        LocalDate newDateOfBirth
    ) {
    }

    public record Deleted(
        UUID userAccountId
    ) {
    }

    // =========================
    // STATUS
    // =========================

    public record Activated(
        UUID userAccountId
    ) {
    }

    public record Deactivated(
        UUID userAccountId
    ) {
    }

    public record Suspended(
        UUID userAccountId,
        String reason
    ) {
    }

    // =========================
    // BUSINESS
    // =========================

    public record PasswordChanged(
        UUID userAccountId,
        String oldPasswordHash,
        String newPasswordHash
    ) {
    }

    public record Locked(
        UUID userAccountId,
        String reason
    ) {
    }

    public record Unlocked(
        UUID userAccountId
    ) {
    }
}
