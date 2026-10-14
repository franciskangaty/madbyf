package dev.madbyf.auth.user.saga.domain;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import dev.madbyf.auth.user.saga.api.commands.UserRegistrationCommand;
import dev.madbyf.auth.user.saga.domain.events.UserRegistrationEvent;
import dev.madbyf.auth.user.util.RegistrationStep;

@Aggregate 
public class UserRegistrationAggregate {
    @AggregateIdentifier 
    private UUID registrationId;
    private RegistrationStep currentStep;

    // Required by Axon
    protected UserRegistrationAggregate() {}

    @CommandHandler 
    public UserRegistrationAggregate(UserRegistrationCommand.StartRegistration command) {
        this.registrationId = command.registrationId();
        
        AggregateLifecycle.apply(new UserRegistrationEvent.Started(
            command.registrationId(),
            command.firstName(),
            command.middleName(),
            command.lastName(),
            command.username(),
            command.contacts(),
            command.dateOfBirth(),
            command.passwordHash(),
            command.requestIpAddress(),
            command.requestDeviceInfo()
        ));
    }

    @CommandHandler 
    public void handle(UserRegistrationCommand.Pass command) {
        AggregateLifecycle.apply(new UserRegistrationEvent.Passed(
            command.registrationId(),
            command.completedStep()
        ));
    }

    @CommandHandler 
    public void handle(UserRegistrationCommand.Cancel command) {
        AggregateLifecycle.apply(new UserRegistrationEvent.Cancelled(
            command.registrationId(),
            currentStep
        ));
    }

    @CommandHandler 
    public void handle(UserRegistrationCommand.Fail command) {
        AggregateLifecycle.apply(new UserRegistrationEvent.Failed(
            command.registrationId(),
            command.failedStep(),
            command.reason()
        ));
    }

    @CommandHandler 
    public void handle(UserRegistrationCommand.Complete command) {
        AggregateLifecycle.apply(new UserRegistrationEvent.Completed(
            command.registrationId()
        ));
    }

    // ---- Event sourcing handlers ----

    @EventSourcingHandler 
    public void on(UserRegistrationEvent.Started event) {
        this.registrationId = event.registrationId();
        this.currentStep = RegistrationStep.STARTED;
    }

    @EventSourcingHandler 
    public void on(UserRegistrationEvent.Passed event) {
        this.currentStep = event.completedStep();
    }

    @EventSourcingHandler 
    public void on(UserRegistrationEvent.Cancelled event) {
        this.currentStep = null; // Registration cancelled, no further steps
    }

    @EventSourcingHandler 
    public void on(UserRegistrationEvent.Failed event) {
        this.currentStep = event.failedStep(); // Registration failed at this step
    }

    @EventSourcingHandler 
    public void on(UserRegistrationEvent.Completed event) {
        this.currentStep = null; // Registration completed, no further steps
    }
}
