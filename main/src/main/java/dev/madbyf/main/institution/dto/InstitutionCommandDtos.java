package dev.madbyf.main.institution.dto;

import java.time.Instant;
import java.time.LocalDate;

public final class InstitutionCommandDtos {

   public record RegisterInstitutionRequest(
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
   
   public record UpdateInstitutionRequest(
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
   
   public record ChangeOwnershipRequest(
           String ownershipType
   ) {}
   
   public record ChangeIdentityRequest(
           String legalName,
           String displayName,
           String shortName
   ) {}
   
   public record ChangeContactRequest(
           String primaryEmail,
           String primaryPhone
   ) {}
   
   public record ChangeAddressRequest(
           String country,
           String region,
           String city,
           String address,
           String postalCode
   ) {}
   
   public record ChangeTimezoneRequest(
           String timezone
   ) {}
   
   public record ChangeLanguageRequest(
           String language
   ) {}
   
   public record ChangeCurrencyRequest(
           String currency
   ) {}
   
   public record VerifyInstitutionRequest(
           String verificationMethod,
           String verificationReference
   ) {}
   
   public record SuspendInstitutionRequest(
           String suspensionReason,
           Instant suspendedUntil
   ) {}
   
   public record RejectInstitutionRequest(
           String reason
   ) {}
}