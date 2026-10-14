package dev.madbyf.auth.user.command.api.commands;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class UserProfileCommand {

    private UserProfileCommand() {
    }

    // =========================
    // CRUD
    // =========================

    public record Create(
            @TargetAggregateIdentifier
            UUID userProfileId,
            UUID userAccountId,
            String displayName,
            String firstName,
            String middleName,
            String lastName
    ) {
    }

    public record Update(
            @TargetAggregateIdentifier
            UUID userProfileId,
            String displayName,
            String firstName,
            String middleName,
            String lastName
    ) {
    }

    public record Delete(
            @TargetAggregateIdentifier
            UUID userProfileId
    ) {
    }

    // =========================
    // STATUS
    // =========================

    public record Activate(
            @TargetAggregateIdentifier
            UUID userProfileId
    ) {
    }

    public record Deactivate(
            @TargetAggregateIdentifier
            UUID userProfileId
    ) {
    }

    public record ChangeVisibility(
        @TargetAggregateIdentifier 
        UUID userProfileId,
        String visibility
    ) {
    }

    // =========================
    // BUSINESS
    // =========================

//     public record ChangeEmail(
//             @TargetAggregateIdentifier
//             UUID userProfileId,
//             String email
//     ) {
//     }

//     public record ChangePhone(
//             @TargetAggregateIdentifier
//             UUID userProfileId,
//             String phoneNumber
//     ) {
//     }

}
