package dev.madbyf.main.institution.projection;

import java.util.UUID;
import java.util.function.Function;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import dev.madbyf.main.institution.event.InstitutionEvents.*;
import dev.madbyf.main.institution.repository.InstitutionViewRepository;
import dev.madbyf.main.institution.view.InstitutionStatus;
import dev.madbyf.main.institution.view.InstitutionView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class InstitutionProjection {

    private final InstitutionViewRepository repository;

    @EventHandler
    public Mono<Void> on(InstitutionRegistered e) {

        return repository.save(
                InstitutionView.builder()
                        .institutionId(e.institutionId())
                        .registrationNumber(e.registrationNumber())
                        .legalName(e.legalName())
                        .displayName(e.displayName())
                        .shortName(e.shortName())
                        .ownershipType(e.ownershipType())
                        .incorporationDate(e.incorporationDate())
                        .country(e.country())
                        .region(e.region())
                        .city(e.city())
                        .address(e.address())
                        .postalCode(e.postalCode())
                        .timezone(e.timezone())
                        .defaultLanguage(e.defaultLanguage())
                        .defaultCurrency(e.defaultCurrency())
                        .website(e.website())
                        .primaryEmail(e.primaryEmail())
                        .primaryPhone(e.primaryPhone())
                        .status(InstitutionStatus.PENDING)
                        .registrationVerified(false)
                        .build()
        ).then();
    }

    @EventHandler
    public Mono<Void> on(InstitutionOwnershipChanged e) {
        return update(e.institutionId(),
                v -> v.withOwnershipType(e.newOwnershipType()));
    }

    @EventHandler
    public Mono<Void> on(InstitutionIdentityChanged e) {
        return update(e.institutionId(), v ->
                v.withLegalName(e.newLegalName())
                 .withDisplayName(e.newDisplayName())
                 .withShortName(e.newShortName()));
    }

    @EventHandler
    public Mono<Void> on(InstitutionContactInformationChanged e) {
        return update(e.institutionId(), v ->
                v.withPrimaryEmail(e.newPrimaryEmail())
                 .withPrimaryPhone(e.newPrimaryPhone()));
    }

    @EventHandler
    public Mono<Void> on(InstitutionAddressChanged e) {
        return update(e.institutionId(), v ->
                v.withCountry(e.newCountry())
                 .withRegion(e.newRegion())
                 .withCity(e.newCity())
                 .withAddress(e.newAddress())
                 .withPostalCode(e.newPostalCode()));
    }

    @EventHandler
    public Mono<Void> on(InstitutionTimezoneChanged e) {
        return update(e.institutionId(),
                v -> v.withTimezone(e.newTimezone()));
    }

    @EventHandler
    public Mono<Void> on(InstitutionDefaultLanguageChanged e) {
        return update(e.institutionId(),
                v -> v.withDefaultLanguage(e.newDefaultLanguage()));
    }

    @EventHandler
    public Mono<Void> on(InstitutionDefaultCurrencyChanged e) {
        return update(e.institutionId(),
                v -> v.withDefaultCurrency(e.newDefaultCurrency()));
    }

    @EventHandler
    public Mono<Void> on(InstitutionRegistrationVerified e) {
        return update(e.institutionId(), v ->
                v.withRegistrationVerified(true)
                 .withStatus(InstitutionStatus.ACTIVE));
    }

    @EventHandler
    public Mono<Void> on(InstitutionActivated e) {
        return update(e.institutionId(),
                v -> v.withStatus(InstitutionStatus.ACTIVE));
    }

    @EventHandler
    public Mono<Void> on(InstitutionSuspended e) {
        return update(e.institutionId(), v ->
                v.withStatus(InstitutionStatus.SUSPENDED)
                 .withSuspensionReason(e.suspensionReason())
                 .withSuspendedUntil(e.suspendedUntil()));
    }

    @EventHandler
    public Mono<Void> on(InstitutionClosed e) {
        return update(e.institutionId(),
                v -> v.withStatus(InstitutionStatus.CLOSED));
    }

    @EventHandler
    public Mono<Void> on(InstitutionReopened e) {
        return update(e.institutionId(),
                v -> v.withStatus(InstitutionStatus.ACTIVE));
    }

    @EventHandler
    public Mono<Void> on(InstitutionArchived e) {
        return update(e.institutionId(),
                v -> v.withStatus(InstitutionStatus.ARCHIVED));
    }

    @EventHandler
    public Mono<Void> on(InstitutionDeleted e) {
        return update(e.institutionId(),
                v -> v.withStatus(InstitutionStatus.DELETED));
    }

    @EventHandler
    public Mono<Void> on(InstitutionRegistrationRejected e) {
        return update(e.institutionId(), v ->
                v.withStatus(InstitutionStatus.REJECTED)
                 .withRegistrationVerified(false));
    }

    @EventHandler
    public Mono<Void> on(InstitutionRestored e) {
        return update(e.institutionId(),
                v -> v.withStatus(InstitutionStatus.ACTIVE));
    }

    private Mono<Void> update(
            UUID institutionId,
            Function<InstitutionView, InstitutionView> updater) {

        return repository.findById(institutionId)
                .map(updater)
                .flatMap(repository::save)
                .then();
    }
}