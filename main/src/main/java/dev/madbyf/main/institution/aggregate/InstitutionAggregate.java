package dev.madbyf.main.institution.aggregate;

import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import dev.madbyf.main.institution.command.InstitutionCommands;
import dev.madbyf.main.institution.event.InstitutionEvents;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class InstitutionAggregate {

    @AggregateIdentifier
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

    private boolean registrationVerified;
    private boolean active;
    private boolean suspended;
    private boolean archived;
    private boolean deleted;
    private boolean closed;

    protected InstitutionAggregate() {
    }

    // =========================================================================
    // Register
    // =========================================================================

    @CommandHandler
    public InstitutionAggregate(InstitutionCommands.RegisterInstitution command) {

        apply(new InstitutionEvents.InstitutionRegistered(
                command.institutionId(),
                command.registrationNumber(),
                command.legalName(),
                command.displayName(),
                command.shortName(),
                command.ownershipType(),
                command.incorporationDate(),
                command.country(),
                command.region(),
                command.city(),
                command.address(),
                command.postalCode(),
                command.timezone(),
                command.defaultLanguage(),
                command.defaultCurrency(),
                command.website(),
                command.primaryEmail(),
                command.primaryPhone()
        ));
    }

    // =========================================================================
    // CRUD
    // =========================================================================

    @CommandHandler
    public void handle(InstitutionCommands.UpdateInstitutionDetails command) {

        ensureNotDeleted();

        apply(new InstitutionEvents.InstitutionDetailsUpdated(
                institutionId,
                command.legalName(),
                command.displayName(),
                command.shortName(),
                command.ownershipType(),
                command.incorporationDate(),
                command.country(),
                command.region(),
                command.city(),
                command.address(),
                command.postalCode(),
                command.timezone(),
                command.defaultLanguage(),
                command.defaultCurrency(),
                command.website(),
                command.primaryEmail(),
                command.primaryPhone()
        ));
    }

    @CommandHandler
    public void handle(InstitutionCommands.ChangeInstitutionOwnership command) {

        ensureNotDeleted();

        if (ownershipType.equals(command.newOwnershipType()))
            return;

        apply(new InstitutionEvents.InstitutionOwnershipChanged(
                institutionId,
                ownershipType,
                command.newOwnershipType()
        ));
    }

    @CommandHandler
    public void handle(InstitutionCommands.ChangeInstitutionIdentity command) {

        ensureNotDeleted();

        apply(new InstitutionEvents.InstitutionIdentityChanged(
                institutionId,
                legalName,
                displayName,
                shortName,
                command.newLegalName(),
                command.newDisplayName(),
                command.newShortName()
        ));
    }

    @CommandHandler
    public void handle(InstitutionCommands.ChangeInstitutionContactInformation command) {

        ensureNotDeleted();

        apply(new InstitutionEvents.InstitutionContactInformationChanged(
                institutionId,
                primaryEmail,
                primaryPhone,
                command.newPrimaryEmail(),
                command.newPrimaryPhone()
        ));
    }

    @CommandHandler
    public void handle(InstitutionCommands.ChangeInstitutionAddress command) {

        ensureNotDeleted();

        apply(new InstitutionEvents.InstitutionAddressChanged(
                institutionId,
                country,
                region,
                city,
                address,
                postalCode,
                command.newCountry(),
                command.newRegion(),
                command.newCity(),
                command.newAddress(),
                command.newPostalCode()
        ));
    }

    @CommandHandler
    public void handle(InstitutionCommands.ChangeInstitutionTimezone command) {

        ensureNotDeleted();

        apply(new InstitutionEvents.InstitutionTimezoneChanged(
                institutionId,
                timezone,
                command.newTimezone()
        ));
    }

    @CommandHandler
    public void handle(InstitutionCommands.ChangeInstitutionDefaultLanguage command) {

        ensureNotDeleted();

        apply(new InstitutionEvents.InstitutionDefaultLanguageChanged(
                institutionId,
                defaultLanguage,
                command.newDefaultLanguage()
        ));
    }

    @CommandHandler
    public void handle(InstitutionCommands.ChangeInstitutionDefaultCurrency command) {

        ensureNotDeleted();

        apply(new InstitutionEvents.InstitutionDefaultCurrencyChanged(
                institutionId,
                defaultCurrency,
                command.newDefaultCurrency()
        ));
    }

    @CommandHandler
    public void handle(InstitutionCommands.DeleteInstitution command) {

        ensureNotDeleted();

        apply(new InstitutionEvents.InstitutionDeleted(institutionId));
    }

    // =========================================================================
    // State
    // =========================================================================

    @CommandHandler
    public void handle(InstitutionCommands.VerifyInstitutionRegistration command) {

        ensureNotDeleted();

        if (registrationVerified)
            throw new IllegalStateException("Institution is already verified.");

        apply(new InstitutionEvents.InstitutionRegistrationVerified(
                institutionId,
                command.verificationMethod(),
                command.verificationReference()
        ));
    }

    @CommandHandler
    public void handle(InstitutionCommands.ActivateInstitution command) {

        ensureNotDeleted();

        if (active)
            return;

        apply(new InstitutionEvents.InstitutionActivated(institutionId));
    }

    @CommandHandler
    public void handle(InstitutionCommands.SuspendInstitution command) {

        ensureNotDeleted();

        if (suspended)
            throw new IllegalStateException("Institution is already suspended.");

        apply(new InstitutionEvents.InstitutionSuspended(
                institutionId,
                command.suspensionReason(),
                command.suspendedUntil()
        ));
    }

    @CommandHandler
    public void handle(InstitutionCommands.CloseInstitution command) {

        ensureNotDeleted();

        apply(new InstitutionEvents.InstitutionClosed(institutionId));
    }

    @CommandHandler
    public void handle(InstitutionCommands.ReopenInstitution command) {

        ensureNotDeleted();

        if (!closed)
            throw new IllegalStateException("Institution is not closed.");

        apply(new InstitutionEvents.InstitutionReopened(institutionId));
    }

    // =========================================================================
    // Logic
    // =========================================================================

    @CommandHandler
    public void handle(InstitutionCommands.RejectInstitutionRegistration command) {

        ensureNotDeleted();

        apply(new InstitutionEvents.InstitutionRegistrationRejected(
                institutionId,
                command.reason()
        ));
    }

    @CommandHandler
    public void handle(InstitutionCommands.RestoreInstitution command) {

        if (!deleted)
            throw new IllegalStateException("Institution is not deleted.");

        apply(new InstitutionEvents.InstitutionRestored(institutionId));
    }

    @CommandHandler
    public void handle(InstitutionCommands.ArchiveInstitution command) {

        ensureNotDeleted();

        if (archived)
            return;

        apply(new InstitutionEvents.InstitutionArchived(institutionId));
    }

    // =========================================================================
    // Event Sourcing Handlers
    // =========================================================================

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionRegistered e) {
        institutionId = e.institutionId();
        registrationNumber = e.registrationNumber();
        legalName = e.legalName();
        displayName = e.displayName();
        shortName = e.shortName();
        ownershipType = e.ownershipType();
        incorporationDate = e.incorporationDate();
        country = e.country();
        region = e.region();
        city = e.city();
        address = e.address();
        postalCode = e.postalCode();
        timezone = e.timezone();
        defaultLanguage = e.defaultLanguage();
        defaultCurrency = e.defaultCurrency();
        website = e.website();
        primaryEmail = e.primaryEmail();
        primaryPhone = e.primaryPhone();
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionDetailsUpdated e) {
        legalName = e.legalName();
        displayName = e.displayName();
        shortName = e.shortName();
        ownershipType = e.ownershipType();
        incorporationDate = e.incorporationDate();
        country = e.country();
        region = e.region();
        city = e.city();
        address = e.address();
        postalCode = e.postalCode();
        timezone = e.timezone();
        defaultLanguage = e.defaultLanguage();
        defaultCurrency = e.defaultCurrency();
        website = e.website();
        primaryEmail = e.primaryEmail();
        primaryPhone = e.primaryPhone();
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionOwnershipChanged e) {
        ownershipType = e.newOwnershipType();
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionIdentityChanged e) {
        legalName = e.newLegalName();
        displayName = e.newDisplayName();
        shortName = e.newShortName();
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionContactInformationChanged e) {
        primaryEmail = e.newPrimaryEmail();
        primaryPhone = e.newPrimaryPhone();
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionAddressChanged e) {
        country = e.newCountry();
        region = e.newRegion();
        city = e.newCity();
        address = e.newAddress();
        postalCode = e.newPostalCode();
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionTimezoneChanged e) {
        timezone = e.newTimezone();
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionDefaultLanguageChanged e) {
        defaultLanguage = e.newDefaultLanguage();
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionDefaultCurrencyChanged e) {
        defaultCurrency = e.newDefaultCurrency();
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionRegistrationVerified e) {
        registrationVerified = true;
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionActivated e) {
        active = true;
        suspended = false;
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionSuspended e) {
        suspended = true;
        active = false;
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionClosed e) {
        closed = true;
        active = false;
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionReopened e) {
        closed = false;
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionArchived e) {
        archived = true;
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionDeleted e) {
        deleted = true;
        AggregateLifecycle.markDeleted();
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionRestored e) {
        deleted = false;
    }

    @EventSourcingHandler
    public void on(InstitutionEvents.InstitutionRegistrationRejected e) {
        registrationVerified = false;
    }

    // =========================================================================

    private void ensureNotDeleted() {
        if (deleted) {
            throw new IllegalStateException("Institution has been deleted.");
        }
    }
}