package dev.madbyf.auth.common.command.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import dev.madbyf.auth.common.util.OwnerRef;

/**
 * Commands for the Username aggregate (common module).
 * Shared by users, institutions, and groups.
 *
 * Lifecycle: Reserve -> Assign (happy path)
 *            Reserve -> CancelReservation | ExpireReservation (abandoned/timed out)
 *            Assign -> Release (frees the username, e.g. for modification flows)
 */
public final class UsernameAggregateCommand {

    private UsernameAggregateCommand() {}

    // ---- CRUD ----

    /** Reserves a username for a holder (registration process, modification saga, etc.) before it is assigned to an owner. */
    public record Reserve(
            @TargetAggregateIdentifier String username,
            OwnerRef ownerRef
    ) {}

    public record MoveReservation(
            @TargetAggregateIdentifier String username,
            OwnerRef ownerRef
    ) {}

    /** Explicitly cancels an active reservation (e.g. saga compensation). */
    public record CancelReservation(
            @TargetAggregateIdentifier String username,
            String reason
    ) {}

    // ---- Status modifiers ----

    /** Cron-generated: fires when a reservation has outlived its deadline without being assigned. */
    public record ExpireReservation(
            @TargetAggregateIdentifier String username
    ) {}

    // ---- Business commands ----

    /** Links a reserved username to its final owner. */
    public record Assign(
            @TargetAggregateIdentifier String username
    ) {}

    /** Frees a currently-assigned username (e.g. step 1 of a username-modification saga). */
    public record Release(
            @TargetAggregateIdentifier String username,
            String reason
    ) {}

}