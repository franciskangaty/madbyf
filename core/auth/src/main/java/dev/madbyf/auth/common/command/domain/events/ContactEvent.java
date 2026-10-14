package dev.madbyf.auth.common.command.domain.events;

import java.util.UUID;

public final class ContactEvent {

    private ContactEvent() {
    }

    // =========================
    // CONTACT
    // =========================

    public record Added(
            String contactValue,
            String contactType,
            String ownerEntity,
            UUID ownerId
    ) {
    }

    public record Verified(
            String contactValue,
            UUID ownerId
    ) {
    }

    public record Removed(
            String contactValue,
            UUID ownerId
    ) {
    }

    // =========================
    // PROFILE LINK
    // =========================

    public record LinkedToProfile(
            String contactValue,
            UUID entityProfileId
    ) {
    }
}
