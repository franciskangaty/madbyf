package dev.madbyf.auth.common.command.domain.events;

import dev.madbyf.auth.common.util.OwnerRef;

public final class UsernameAggregateEvent {

    private UsernameAggregateEvent() {}

    // ---- Reservation lifecycle ----

    /**
     * Published when a username is successfully reserved.
     */
    public record Reserved(
            String username,
            OwnerRef ownerRef
    ) {}

    /**
     * Published when an existing reservation is moved to another owner.
     */
    public record ReservationMoved(
            String username,
            OwnerRef ownerRef
    ) {}

    /**
     * Published when an active reservation is explicitly cancelled.
     */
    public record ReservationCancelled(
            String username,
            String reason
    ) {}

    /**
     * Published when a reservation expires before being assigned.
     */
    public record ReservationExpired(
            String username
    ) {}

    // ---- Assignment lifecycle ----

    /**
     * Published when a reserved username is assigned to its final owner.
     */
    public record Assigned(
            String username
    ) {}

    /**
     * Published when an assigned username is released.
     */
    public record Released(
            String username,
            String reason
    ) {}
}
