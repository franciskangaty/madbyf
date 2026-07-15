package dev.madbyf.main.institution.command;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class InstitutionCommands {

   // ====================================   CRUD  ======================================
    public record RegisterInstitution(
            @TargetAggregateIdentifier UUID institutionId,
            String registrationNumber,
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
            String timezone,
            String defaultLanguage,
            String defaultCurrency,
            String website,
            String primaryEmail,
            String primaryPhone
    ) {}

    public record ChangeInstitutionOwnership(
            @TargetAggregateIdentifier UUID institutionId,
            String newOwnershipType
    ) {}

    public record ChangeInstitutionIdentity(
            @TargetAggregateIdentifier UUID institutionId,
            String newLegalName,
            String newDisplayName,
            String newShortName
    ) {}

    public record ChangeInstitutionContactInformation(
            @TargetAggregateIdentifier UUID institutionId,
            String newPrimaryEmail,
            String newPrimaryPhone
    ) {}

    public record ChangeInstitutionAddress(
            @TargetAggregateIdentifier UUID institutionId,
            String newCountry,
            String newRegion,
            String newCity,
            String newAddress,
            String newPostalCode
    ) {}

    public record ChangeInstitutionTimezone(
            @TargetAggregateIdentifier UUID institutionId,
            String newTimezone
    ) {}

    public record ChangeInstitutionDefaultLanguage(
            @TargetAggregateIdentifier UUID institutionId,
            String newDefaultLanguage
    ) {}

    public record ChangeInstitutionDefaultCurrency(
            @TargetAggregateIdentifier UUID institutionId,
            String newDefaultCurrency
    ) {}

    public record DeleteInstitution(
            @TargetAggregateIdentifier UUID institutionId
    ) {}

    //   ======================= STATE ======================

    public record VerifyInstitutionRegistration(
            @TargetAggregateIdentifier UUID institutionId,
            String verificationMethod,
            String verificationReference
    ) {}

    public record ActivateInstitution(
            @TargetAggregateIdentifier UUID institutionId
    ) {}

    public record SuspendInstitution(
            @TargetAggregateIdentifier UUID institutionId,
            String suspensionReason,
            Instant suspendedUntil
    ) {}

    public record CloseInstitution(
            @TargetAggregateIdentifier UUID institutionId
    ) {}

    public record ReopenInstitution(
            @TargetAggregateIdentifier UUID institutionId
    ) {}
    
    //   ======================= LOGIC =============================

    public record RejectInstitutionRegistration(
            @TargetAggregateIdentifier UUID institutionId,
            String reason
    ) {}

    public record RestoreInstitution(
            @TargetAggregateIdentifier UUID institutionId
    ) {}


    public record ArchiveInstitution(
            @TargetAggregateIdentifier UUID institutionId
    ) {}
    
}
