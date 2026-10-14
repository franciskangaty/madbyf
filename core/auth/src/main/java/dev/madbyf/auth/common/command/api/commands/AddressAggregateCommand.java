package dev.madbyf.auth.common.command.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Commands for the Address aggregate (common module).
 * An address is built by pointing at a GeographicUnit (the deepest level the
 * entity can select, e.g. a street or building) plus optional free details.
 * Country/region/county/city are derived from the unit's ancestors, not stored
 * here. Jurisdiction is likewise derived (governance module maps jurisdictions
 * onto geographic units), so addresses carry no jurisdiction field.
 *
 * Same shared/verified-per-owner shape as Contact: an owner-map and a
 * verified-map. Verification is evidence-backed (proof of address), not OTP.
 */
public final class AddressAggregateCommand {

    private AddressAggregateCommand() {}

    // ---- CRUD ----

    public record Add(
            @TargetAggregateIdentifier String addressId,
            String geographicUnitId, // deepest selected unit (city, street or building)
            String detail,           // nullable: house number, apartment/unit, floor, etc.
            String postalCode        // nullable
    ) {}

    public record Update(
            @TargetAggregateIdentifier String addressId,
            String geographicUnitId,
            String detail,
            String postalCode
    ) {}

    public record Remove(
            @TargetAggregateIdentifier String addressId
    ) {}

    // ---- Status modifiers ----

    /** Cron-generated: a requested verification has outlived its deadline unconfirmed. */
    public record ExpireVerification(
            @TargetAggregateIdentifier String addressId,
            String ownerId
    ) {}

    // ---- Business commands ----

    public record LinkOwner(
            @TargetAggregateIdentifier String addressId,
            String ownerId,
            OwnerType ownerType,
            AddressUse use
    ) {}

    public record UnlinkOwner(
            @TargetAggregateIdentifier String addressId,
            String ownerId
    ) {}

    public record RequestVerification(
            @TargetAggregateIdentifier String addressId,
            String ownerId,
            String evidenceReference
    ) {}

    public record VerifyAddress(
            @TargetAggregateIdentifier String addressId,
            String ownerId,
            String verifiedByUserId,
            String verificationMethod
    ) {}

    public record RejectVerification(
            @TargetAggregateIdentifier String addressId,
            String ownerId,
            String reason
    ) {}

    public enum OwnerType { USER, INSTITUTION, GROUP }
    public enum AddressUse { RESIDENTIAL, MAILING, REGISTERED_OFFICE, BILLING }
}