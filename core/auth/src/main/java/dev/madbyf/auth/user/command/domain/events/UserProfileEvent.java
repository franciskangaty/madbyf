package dev.madbyf.auth.user.command.domain.events;

import java.util.UUID;

public final class UserProfileEvent {

    private UserProfileEvent() {
    }

    // =========================
    // CRUD
    // =========================

    public record Created(
            UUID userProfileId,
            UUID userAccountId,
            String displayName,
            String firstName,
            String middleName,
            String lastName
    ) {
    }

    public record Updated(
            UUID userProfileId,

            String oldDisplayName,
            String newDisplayName,

            String oldFirstName,
            String newFirstName,

            String oldMiddleName,
            String newMiddleName,

            String oldLastName,
            String newLastName
    ) {
    }

    public record Deleted(
            UUID userProfileId
    ) {
    }

    // =========================
    // STATUS
    // =========================

    public record Activated(
            UUID userProfileId
    ) {
    }

    public record Deactivated(
            UUID userProfileId
    ) {
    }

    public record VisibilityChanged(
            UUID userProfileId,
            String oldVisibility,
            String newVisibility
    ) {
    }
}
