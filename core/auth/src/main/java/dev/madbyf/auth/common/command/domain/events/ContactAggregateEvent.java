package dev.madbyf.auth.common.command.domain.events;

import dev.madbyf.auth.common.util.OwnerRef;

public final class ContactAggregateEvent {

    private ContactAggregateEvent() {}

    // ---- CRUD ----

    /**
     * Published when a new contact value is registered.
     */
    public record Added(
            String contact,
            String type
    ) {}

    public record AddedWithOwner(
            String contact,
            String type,
            OwnerRef ownerRef
    ) {}

    /**
     * Published when a contact value is completely removed.
     */
    public record Removed(
            String contact
    ) {}

    // ---- Verification lifecycle ----

    /**
     * Published when a pending verification request expires.
     */
    public record VerificationExpired(
            String contact,
            OwnerRef ownerRef
    ) {}

    // ---- Owner lifecycle ----

    /**
     * Published when a contact is linked to an owner.
     * The owner starts as unverified.
     */
    public record OwnerLinked(
            String contact,
            OwnerRef ownerRef
    ) {}

    /**
     * Published when a contact is unlinked from an owner.
     */
    public record OwnerUnlinked(
            String contact,
            OwnerRef ownerRef
    ) {}

    // ---- Verification ----

    /**
     * Published when verification has been requested for an owner.
     */
    public record VerificationRequested(
            String contact,
            OwnerRef ownerRef
    ) {}

    /**
     * Published when the submitted verification code is confirmed.
     */
    public record Verified(
            String contact,
            OwnerRef ownerRef
    ) {}
}
