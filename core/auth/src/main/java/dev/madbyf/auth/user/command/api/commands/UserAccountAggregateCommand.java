package dev.madbyf.auth.user.command.api.commands;

import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Commands for the UserAccount aggregate.
 * Covers profile info and the full Spring Security principal state, so that
 * other modules (fraud detection, security) and business processes can act
 * on it directly rather than re-deriving it elsewhere.
 */
public final class UserAccountAggregateCommand {

    private UserAccountAggregateCommand() {}

    // ---- CRUD ----

    /** Created once contact verification has completed in the registration saga. Username/contacts already exist by this point. */
    public record Create(
            @TargetAggregateIdentifier UUID userId,
            String firstName,
            String middleName,
            String lastName,
            LocalDate dateOfBirth,
                String passwordHash
    ) {}

    public record UpdateNames(
            @TargetAggregateIdentifier UUID userId,
            String firstName,
                String middleName,
            String lastName
    ) {}

        public record UpdateDateOfBirth(
                @TargetAggregateIdentifier UUID userId,
                LocalDate dateOfBirth
        ) {}

        public record UpdatePassword(
                @TargetAggregateIdentifier UUID userId,
                String oldPasswordHash,
                String newPasswordHash
        ) {}

    public record SetProfilePhotoUrl(
            @TargetAggregateIdentifier UUID userId,
            String profilePhotoUrl
    ) {}

    // ---- Verification (distinct from contact OTP verification, e.g. document/ID-check backed) ----

    public record VerifyName(
            @TargetAggregateIdentifier UUID userId,
            String verificationMethod
    ) {}

    public record VerifyAge(
            @TargetAggregateIdentifier UUID userId,
            String verificationMethod
    ) {}

    // ---- Status modifiers: Spring Security principal state ----

    public record SetPassword(
            @TargetAggregateIdentifier UUID userId,
            String encodedPassword
    ) {}

    public record RemovePassword(
            @TargetAggregateIdentifier UUID userId
    ) {}

    public record Enable(
            @TargetAggregateIdentifier UUID userId
    ) {}

    public record Disable(
            @TargetAggregateIdentifier UUID userId,
            String reason
    ) {}

    public record Lock(
            @TargetAggregateIdentifier UUID userId,
            String reason
    ) {}

    public record Unlock(
            @TargetAggregateIdentifier UUID userId
    ) {}

    public record ExpireCredentials(
            @TargetAggregateIdentifier UUID userId
    ) {}

    public record RenewCredentials(
            @TargetAggregateIdentifier UUID userId
    ) {}

    public record ExpireAccount(
            @TargetAggregateIdentifier UUID userId
    ) {}

    public record ReactivateAccount(
            @TargetAggregateIdentifier UUID userId
    ) {}
}