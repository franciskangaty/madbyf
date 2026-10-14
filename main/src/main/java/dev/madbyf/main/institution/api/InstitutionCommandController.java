package dev.madbyf.main.institution.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.madbyf.main.institution.dto.InstitutionCommandDtos.*;
import dev.madbyf.main.institution.command.InstitutionCommands.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/institutions")
@RequiredArgsConstructor
public class InstitutionCommandController {

    private final CommandGateway commandGateway;

    // ------------------------------------------------------------------------
    // CRUD
    // ------------------------------------------------------------------------

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CompletableFuture<UUID> register(
            @Valid @RequestBody RegisterInstitutionRequest request) {

        UUID institutionId = UUID.randomUUID();

        return commandGateway.send(new RegisterInstitution(
                institutionId,
                request.registrationNumber(),
                request.legalName(),
                request.displayName(),
                request.shortName(),
                request.ownershipType(),
                request.incorporationDate(),
                request.country(),
                request.region(),
                request.city(),
                request.address(),
                request.postalCode(),
                request.timezone(),
                request.defaultLanguage(),
                request.defaultCurrency(),
                request.website(),
                request.primaryEmail(),
                request.primaryPhone()
        ));
    }

    @PutMapping("/{institutionId}")
    public CompletableFuture<Void> update(
            @PathVariable UUID institutionId,
            @Valid @RequestBody UpdateInstitutionRequest request) {

        return commandGateway.send(new UpdateInstitutionDetails(
                institutionId,
                request.legalName(),
                request.displayName(),
                request.shortName(),
                request.ownershipType(),
                request.incorporationDate(),
                request.country(),
                request.region(),
                request.city(),
                request.address(),
                request.postalCode(),
                request.timezone(),
                request.defaultLanguage(),
                request.defaultCurrency(),
                request.website(),
                request.primaryEmail(),
                request.primaryPhone()
        ));
    }

    @PatchMapping("/{institutionId}/ownership")
    public CompletableFuture<Void> changeOwnership(
            @PathVariable UUID institutionId,
            @RequestBody ChangeOwnershipRequest request) {

        return commandGateway.send(
                new ChangeInstitutionOwnership(
                        institutionId,
                        request.ownershipType()
                ));
    }

    @PatchMapping("/{institutionId}/identity")
    public CompletableFuture<Void> changeIdentity(
            @PathVariable UUID institutionId,
            @RequestBody ChangeIdentityRequest request) {

        return commandGateway.send(
                new ChangeInstitutionIdentity(
                        institutionId,
                        request.legalName(),
                        request.displayName(),
                        request.shortName()
                ));
    }

    @PatchMapping("/{institutionId}/contact")
    public CompletableFuture<Void> changeContact(
            @PathVariable UUID institutionId,
            @RequestBody ChangeContactRequest request) {

        return commandGateway.send(
                new ChangeInstitutionContactInformation(
                        institutionId,
                        request.primaryEmail(),
                        request.primaryPhone()
                ));
    }

    @PatchMapping("/{institutionId}/address")
    public CompletableFuture<Void> changeAddress(
            @PathVariable UUID institutionId,
            @RequestBody ChangeAddressRequest request) {

        return commandGateway.send(
                new ChangeInstitutionAddress(
                        institutionId,
                        request.country(),
                        request.region(),
                        request.city(),
                        request.address(),
                        request.postalCode()
                ));
    }

    @PatchMapping("/{institutionId}/timezone")
    public CompletableFuture<Void> changeTimezone(
            @PathVariable UUID institutionId,
            @RequestBody ChangeTimezoneRequest request) {

        return commandGateway.send(
                new ChangeInstitutionTimezone(
                        institutionId,
                        request.timezone()
                ));
    }

    @PatchMapping("/{institutionId}/language")
    public CompletableFuture<Void> changeLanguage(
            @PathVariable UUID institutionId,
            @RequestBody ChangeLanguageRequest request) {

        return commandGateway.send(
                new ChangeInstitutionDefaultLanguage(
                        institutionId,
                        request.language()
                ));
    }

    @PatchMapping("/{institutionId}/currency")
    public CompletableFuture<Void> changeCurrency(
            @PathVariable UUID institutionId,
            @RequestBody ChangeCurrencyRequest request) {

        return commandGateway.send(
                new ChangeInstitutionDefaultCurrency(
                        institutionId,
                        request.currency()
                ));
    }

    @DeleteMapping("/{institutionId}")
    public CompletableFuture<Void> delete(
            @PathVariable UUID institutionId) {

        return commandGateway.send(new DeleteInstitution(institutionId));
    }

    // ------------------------------------------------------------------------
    // STATE
    // ------------------------------------------------------------------------

    @PostMapping("/{institutionId}/verify")
    public CompletableFuture<Void> verify(
            @PathVariable UUID institutionId,
            @RequestBody VerifyInstitutionRequest request) {

        return commandGateway.send(
                new VerifyInstitutionRegistration(
                        institutionId,
                        request.verificationMethod(),
                        request.verificationReference()
                ));
    }

    @PostMapping("/{institutionId}/activate")
    public CompletableFuture<Void> activate(
            @PathVariable UUID institutionId) {

        return commandGateway.send(
                new ActivateInstitution(institutionId));
    }

    @PostMapping("/{institutionId}/suspend")
    public CompletableFuture<Void> suspend(
            @PathVariable UUID institutionId,
            @RequestBody SuspendInstitutionRequest request) {

        return commandGateway.send(
                new SuspendInstitution(
                        institutionId,
                        request.suspensionReason(),
                        request.suspendedUntil()
                ));
    }

    @PostMapping("/{institutionId}/close")
    public CompletableFuture<Void> close(
            @PathVariable UUID institutionId) {

        return commandGateway.send(
                new CloseInstitution(institutionId));
    }

    @PostMapping("/{institutionId}/reopen")
    public CompletableFuture<Void> reopen(
            @PathVariable UUID institutionId) {

        return commandGateway.send(
                new ReopenInstitution(institutionId));
    }

    // ------------------------------------------------------------------------
    // LOGIC
    // ------------------------------------------------------------------------

    @PostMapping("/{institutionId}/reject")
    public CompletableFuture<Void> reject(
            @PathVariable UUID institutionId,
            @RequestBody RejectInstitutionRequest request) {

        return commandGateway.send(
                new RejectInstitutionRegistration(
                        institutionId,
                        request.reason()
                ));
    }

    @PostMapping("/{institutionId}/restore")
    public CompletableFuture<Void> restore(
            @PathVariable UUID institutionId) {

        return commandGateway.send(
                new RestoreInstitution(institutionId));
    }

    @PostMapping("/{institutionId}/archive")
    public CompletableFuture<Void> archive(
            @PathVariable UUID institutionId) {

        return commandGateway.send(
                new ArchiveInstitution(institutionId));
    }
}