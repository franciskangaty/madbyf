import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class InstitutionCommands {

    // ==================================== CRUD ======================================

    public record RegisterInstitution(
            @TargetAggregateIdentifier UUID institutionId,
            String registrationNumber,
            UUID institutionTypeId,
            String legalName,
            String displayName,
            String shortName,
            String ownershipType,
            LocalDate incorporationDate,
            String country,
            String region,
            String city,
            String address,
            String postalCode,
            String timezone,
            String defaultLanguage,
            String defaultCurrency,
            String website,
            String primaryEmail,
            String primaryPhone
    ) {}

    public record UpdateInstitutionDetails(
            @TargetAggregateIdentifier UUID institutionId,
            String legalName,
            String displayName,
            String shortName,
            String ownershipType,
            LocalDate incorporationDate,
            String country,
            String region,
            String city,
            String address,
            String postalCode,
            String website,
            String primaryEmail,
            String primaryPhone
    ) {}

    public record DeleteInstitution(
            @TargetAggregateIdentifier UUID institutionId
    ) {}

    // ==================================== IDENTITY ======================================

    public record ChangeInstitutionIdentity(
            @TargetAggregateIdentifier UUID institutionId,
            String legalName,
            String displayName,
            String shortName
    ) {}

    public record ChangeInstitutionOwnership(
            @TargetAggregateIdentifier UUID institutionId,
            String ownershipType
    ) {}

    // ==================================== CONTACT ======================================

    public record ChangeInstitutionContactInformation(
            @TargetAggregateIdentifier UUID institutionId,
            String primaryEmail,
            String primaryPhone,
            String website
    ) {}

    public record ChangeInstitutionAddress(
            @TargetAggregateIdentifier UUID institutionId,
            String country,
            String region,
            String city,
            String address,
            String postalCode,
            Double latitude,
            Double longitude
    ) {}

    public record ChangeInstitutionTimezone(
            @TargetAggregateIdentifier UUID institutionId,
            String timezone
    ) {}

    public record ChangeInstitutionDefaultLanguage(
            @TargetAggregateIdentifier UUID institutionId,
            String language
    ) {}

    public record ChangeInstitutionDefaultCurrency(
            @TargetAggregateIdentifier UUID institutionId,
            String currency
    ) {}

    public record UpdateInstitutionDescription(
            @TargetAggregateIdentifier UUID institutionId,
            String description
    ) {}

    // ==================================== STATE ======================================

    public record VerifyInstitutionRegistration(
            @TargetAggregateIdentifier UUID institutionId,
            String verificationMethod,
            String verificationReference
    ) {}

    public record RejectInstitutionRegistration(
            @TargetAggregateIdentifier UUID institutionId,
            String reason
    ) {}

    public record ActivateInstitution(
            @TargetAggregateIdentifier UUID institutionId
    ) {}

    public record SuspendInstitution(
            @TargetAggregateIdentifier UUID institutionId,
            String suspensionReason,
            Instant suspendedUntil
    ) {}

    public record ReactivateInstitution(
            @TargetAggregateIdentifier UUID institutionId
    ) {}

    public record ArchiveInstitution(
            @TargetAggregateIdentifier UUID institutionId,
            String archiveReason
    ) {}

    public record RestoreInstitution(
            @TargetAggregateIdentifier UUID institutionId
    ) {}

    public record CloseInstitution(
            @TargetAggregateIdentifier UUID institutionId,
            String closureReason,
            LocalDate effectiveDate
    ) {}

    public record ReopenInstitution(
            @TargetAggregateIdentifier UUID institutionId
    ) {}
}