package dev.madbyf.auth.user.saga.api.commands;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import dev.madbyf.auth.common.util.Contact;
import dev.madbyf.auth.user.util.RegistrationStep;

/**
 * Commands for the UserRegistration aggregate — separate from UserAccount.
 * This aggregate records the saga's progress and is the compensation target
 * on failure; UserAccount itself is only created after contact verification.
 *
 * Saga order:
 *   Register -> fraud check -> ReserveUsername -> AssignUsername
 *     -> AddContact(s) -> RequestContactVerification -> [wait up to 1 week]
 *     -> (on verified) CreateAccount -> AssignRole(USER) -> CompleteRegistration -> notify
 *   Any step's failure -> Fail -> compensates prior steps.
 */
public final class UserRegistrationCommand {

    // ---- CRUD ----

    /** Starts the registration process. Triggers the saga. */
    public record StartRegistration(
        @TargetAggregateIdentifier UUID registrationId,
        String firstName,
        String middleName,
        String lastName,
        String username,
        Set<Contact> contacts,
        LocalDate dateOfBirth,
        String passwordHash,
        String requestIpAddress,
        String requestDeviceInfo
    ) {}

    // ---- Status modifiers: step recording ----

//     public record UpdateStatus(
//         @TargetAggregateIdentifier UUID registrationId,
//         String completedStep
//     ) {}

//     public record RecordFraudCheckPassed(
//             @TargetAggregateIdentifier UUID registrationId
//     ) {}

//     public record RecordFraudCheckFailed(
//             @TargetAggregateIdentifier UUID registrationId,
//             String reason
//     ) {}

//     public record RecordUsernameReserved(
//             @TargetAggregateIdentifier UUID registrationId,
//             String usernameId
//     ) {}

//     public record RecordUsernameReservationFailed(
//             @TargetAggregateIdentifier UUID registrationId,
//             String reason
//     ) {}

//     public record RecordUsernameAssigned(
//             @TargetAggregateIdentifier UUID registrationId,
//             String usernameId
//     ) {}

//     public record RecordContactAdded(
//             @TargetAggregateIdentifier UUID registrationId,
//             String contactId
//     ) {}

//     public record RecordContactVerificationRequested(
//             @TargetAggregateIdentifier UUID registrationId,
//             String contactId
//     ) {}

//     public record RecordContactVerified(
//             @TargetAggregateIdentifier UUID registrationId,
//             String contactId
//     ) {}

//     public record RecordAccountCreated(
//             @TargetAggregateIdentifier UUID registrationId,
//             String userId
//     ) {}

//     public record RecordRoleAssigned(
//             @TargetAggregateIdentifier UUID registrationId,
//             String role
//     ) {}

    // ---- Business commands ----

    /** Fires once role assignment is confirmed; downstream triggers the "notify user" projection. */
    public record Complete(
            @TargetAggregateIdentifier UUID registrationId
    ) {}

    /**
     * Fails the registration at whatever step it reached, carrying enough
     * context for the saga to issue the correct compensating commands
     * (Username.CancelReservation/Release, Contact.UnlinkOwner, etc.).
     */
    public record Fail(
            @TargetAggregateIdentifier UUID registrationId,
            RegistrationStep failedStep,
            String reason
    ) {}

    public record Pass(
            @TargetAggregateIdentifier UUID registrationId,
            RegistrationStep completedStep
    ) {}

    public record Cancel(
            @TargetAggregateIdentifier UUID registrationId
    ) {}

}