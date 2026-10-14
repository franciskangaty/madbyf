package dev.madbyf.auth.user.command.api.commands;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class UserAccountCommand {

    // =========================
    // CRUD
    // =========================

    public record Register(
        @TargetAggregateIdentifier UUID registrationId,
        String firstName,
        String middleName,  
        String lastName,
        String username,
        Map<String, String> contacts,
        LocalDate dateOfBirth,
        String passwordHash
    ) {
    }

    public record Add(
            @TargetAggregateIdentifier
            UUID userAccountId,
            String firstName,
            String middleName,  
            String lastName,
            LocalDate dateOfBirth,
            String passwordHash
    ) {
    }

    public record ModifyInfo(
            @TargetAggregateIdentifier
            UUID userAccountId,
            String firstName,
            String middleName,  
            String lastName,
            LocalDate dateOfBirth
    ) {
    }

    public record Delete(
            @TargetAggregateIdentifier
            UUID userAccountId
    ) {
    }

    // =========================
    // STATUS
    // =========================

    public record Activate(
            @TargetAggregateIdentifier
            UUID userAccountId
    ) {
    }

    public record Deactivate(
            @TargetAggregateIdentifier
            UUID userAccountId
    ) {
    }

    public record Suspend(
            @TargetAggregateIdentifier
            UUID userAccountId,
            String reason
    ) {
    }

    // =========================
    // BUSINESS
    // =========================

    public record ChangePassword(
            @TargetAggregateIdentifier
            UUID userAccountId,
            String oldPasswordHash,
            String newPasswordHash
    ) {
    }

    public record Lock(
            @TargetAggregateIdentifier
            UUID userAccountId,
            String reason
    ) {
    }

    public record Unlock(
            @TargetAggregateIdentifier
            UUID userAccountId
    ) {
    }
}
