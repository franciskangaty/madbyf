package dev.madbyf.main.institution.event;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public final class InstitutionEvents {

    // ==================================== CRUD ====================================

    public record InstitutionRegistered(
            UUID institutionId,
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

    public record InstitutionDetailsUpdated(
            UUID institutionId,
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

    public record InstitutionOwnershipChanged(
            UUID institutionId,
            String previousOwnershipType,
            String newOwnershipType
    ) {}

    public record InstitutionIdentityChanged(
            UUID institutionId,
            String previousLegalName,
            String previousDisplayName,
            String previousShortName,
            String newLegalName,
            String newDisplayName,
            String newShortName
    ) {}

    public record InstitutionContactInformationChanged(
            UUID institutionId,
            String previousPrimaryEmail,
            String previousPrimaryPhone,
            String newPrimaryEmail,
            String newPrimaryPhone
    ) {}

    public record InstitutionAddressChanged(
            UUID institutionId,
            String previousCountry,
            String previousRegion,
            String previousCity,
            String previousAddress,
            String previousPostalCode,
            String newCountry,
            String newRegion,
            String newCity,
            String newAddress,
            String newPostalCode
    ) {}

    public record InstitutionTimezoneChanged(
            UUID institutionId,
            String previousTimezone,
            String newTimezone
    ) {}

    public record InstitutionDefaultLanguageChanged(
            UUID institutionId,
            String previousDefaultLanguage,
            String newDefaultLanguage
    ) {}

    public record InstitutionDefaultCurrencyChanged(
            UUID institutionId,
            String previousDefaultCurrency,
            String newDefaultCurrency
    ) {}

    public record InstitutionDeleted(
            UUID institutionId
    ) {}

    // ==================================== STATE ====================================

    public record InstitutionRegistrationVerified(
            UUID institutionId,
            String verificationMethod,
            String verificationReference
    ) {}

    public record InstitutionActivated(
            UUID institutionId
    ) {}

    public record InstitutionSuspended(
            UUID institutionId,
            String suspensionReason,
            Instant suspendedUntil
    ) {}

    public record InstitutionClosed(
            UUID institutionId
    ) {}

    public record InstitutionReopened(
            UUID institutionId
    ) {}

    // ==================================== LOGIC ====================================

    public record InstitutionRegistrationRejected(
            UUID institutionId,
            String reason
    ) {}

    public record InstitutionRestored(
            UUID institutionId
    ) {}

    public record InstitutionArchived(
            UUID institutionId
    ) {}

}