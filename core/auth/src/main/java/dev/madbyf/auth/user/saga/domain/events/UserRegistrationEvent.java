package dev.madbyf.auth.user.saga.domain.events;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import dev.madbyf.auth.common.util.Contact;
import dev.madbyf.auth.user.util.RegistrationStep;

public final class UserRegistrationEvent {

    // ---- Registration lifecycle ----

    /**
     * Published when the registration process has been started.
     */
    public record Started(
            UUID registrationId,
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

    // ---- Registration completion ----

    /**
     * Published when the entire registration process has completed.
     */
    public record Completed(
            UUID registrationId
    ) {}

    // ---- Registration failure ----

    /**
     * Published when registration fails at a particular step.
     */
        public record Failed(
            UUID registrationId,
            RegistrationStep failedStep,
            String reason
    ) {}

    public record Passed(
            UUID registrationId,
            RegistrationStep completedStep
    ) {}

    public record Cancelled(
            UUID registrationId,
            RegistrationStep cancelledStep
    ) {}
}
