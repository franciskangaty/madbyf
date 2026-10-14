package dev.madbyf.main.institution.view;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Table("institutions")
@Getter
@Builder(toBuilder = true)
@With
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionView {

    @Id
    private UUID institutionId;

    private String registrationNumber;

    private String legalName;
    private String displayName;
    private String shortName;

    private String ownershipType;

    private LocalDate incorporationDate;

    private String country;
    private String region;
    private String city;
    private String address;
    private String postalCode;

    private String timezone;
    private String defaultLanguage;
    private String defaultCurrency;

    private String website;
    private String primaryEmail;
    private String primaryPhone;

    private InstitutionStatus status;
    private boolean registrationVerified;

    private String suspensionReason;
    private Instant suspendedUntil;
}