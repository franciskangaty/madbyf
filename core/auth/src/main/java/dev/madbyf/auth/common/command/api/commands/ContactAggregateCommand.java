package dev.madbyf.auth.common.command.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import dev.madbyf.auth.common.util.OwnerRef;

/**
 * Commands for the Contact aggregate (common module).
 * A contact value (email/phone) is not coupled to a single owner: it holds
 * an owner-map and a verified-map, since one contact can be shared/reused
 * across owners and verified independently per owner.
 */
public final class ContactAggregateCommand {

    private ContactAggregateCommand() {}

    // ---- CRUD ----

    /** Registers a new contact value (creates the aggregate if it doesn't exist for this value). */
        public record Add(
                @TargetAggregateIdentifier String contact,
                String type // add support for email and phone first
        ) {}

    public record AddWithOwner(
            @TargetAggregateIdentifier String contact,
            String type, // add support for email and phone first
            OwnerRef ownerRef
    ) {}

    /** Removes a contact value entirely (only valid once it has no linked owners). */
    public record Remove(
            @TargetAggregateIdentifier String contact
    ) {}

    // ---- Status modifiers ----

    /** Cron-generated: a requested verification has outlived its deadline unconfirmed. */
    public record ExpireVerification(
            @TargetAggregateIdentifier String contact,
            OwnerRef ownerRef
    ) {}

    // ---- Business commands ----

    /** Links this contact to an owner (adds an entry to the owner-map, unverified by default). */
    public record LinkOwner(
            @TargetAggregateIdentifier String contact,
            OwnerRef ownerRef
    ) {}

    /** Unlinks this contact from an owner (removes from both owner-map and verified-map). */
    public record UnlinkOwner(
            @TargetAggregateIdentifier String contact,
            OwnerRef ownerRef
    ) {}

    /** Triggers Spring's OTP generation workflow and marks verification as requested for this owner. */
    public record RequestVerification(
            @TargetAggregateIdentifier String contact,
            OwnerRef ownerRef
    ) {}

    /** Confirms the OTP/code and marks this owner's entry as verified. */
    public record Verify(
            @TargetAggregateIdentifier String contact,
            OwnerRef ownerRef,
            String submittedCode // todo: remove for security
    ) {}

}