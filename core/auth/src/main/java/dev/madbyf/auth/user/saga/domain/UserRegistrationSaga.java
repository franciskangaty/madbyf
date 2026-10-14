package dev.madbyf.auth.user.saga.domain;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;

import dev.madbyf.auth.common.command.api.commands.ContactAggregateCommand;
import dev.madbyf.auth.common.command.api.commands.UsernameAggregateCommand;
import dev.madbyf.auth.common.command.domain.events.ContactAggregateEvent;
import dev.madbyf.auth.common.command.domain.events.UsernameAggregateEvent;
import dev.madbyf.auth.common.util.Contact;
import dev.madbyf.auth.common.util.OwnerRef;
import dev.madbyf.auth.user.command.api.commands.UserAccountAggregateCommand;
import dev.madbyf.auth.user.command.domain.events.UserAccountAggregateEvent;
import dev.madbyf.auth.user.saga.domain.events.UserRegistrationEvent;
import dev.madbyf.auth.notification.command.api.commands.NotificationCommand;

@Saga 
public class UserRegistrationSaga {
    
    private UUID regitrationId;
    private UUID userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String username;
    private Set<Contact> contacts;
    private LocalDate dateOfBirth;
    private String passwordHash;

    private transient CommandGateway commandGateway;    

    @StartSaga 
    @SagaEventHandler(associationProperty = "registrationId")
    public void on(UserRegistrationEvent.Started event) {
        this.userId = UUID.randomUUID();
        this.regitrationId = event.registrationId();
        this.firstName = event.firstName();
        this.middleName = event.middleName();
        this.lastName = event.lastName();
        this.username = event.username();
        this.contacts = event.contacts();
        this.dateOfBirth = event.dateOfBirth();
        this.passwordHash = event.passwordHash();

        commandGateway.send(
            new UsernameAggregateCommand.Reserve(
                this.username,
                new OwnerRef("user", this.userId)
            )
        );
        // Todo: implement error handling
    }

    @SagaEventHandler(associationProperty = "username")
    public void on(UsernameAggregateEvent.Reserved event) {
        commandGateway.send(
            new UsernameAggregateCommand.Assign(
                this.username
            )
        );
    }

    @SagaEventHandler(associationProperty = "username")
    public void on(UsernameAggregateEvent.Assigned event) {
        for (Contact contact : this.contacts) {
            commandGateway.send(
                new ContactAggregateCommand.AddWithOwner(
                    contact.value(),
                    contact.type(),
                    new OwnerRef("user", this.userId)
                )
            );
        }
    }

    @SagaEventHandler(associationProperty = "contact")
    public void on(ContactAggregateEvent.AddedWithOwner event) {
        commandGateway.send(
            new UserAccountAggregateCommand.Create(
                userId,
                firstName,
                middleName,
                lastName,
                dateOfBirth,
                passwordHash
            )
        );
    }

    /* @SagaEventHandler(associationProperty = "registrationId")
    public void on(UserRegistrationEvent.Cancelled event) {
        // Todo: Handle cancellation logic if needed
    } */

    @EndSaga 
    @SagaEventHandler(associationProperty = "userId")
    public void on(UserAccountAggregateEvent.Enabled event) {
        commandGateway.send(
            new NotificationCommand.Create(
                UUID.randomUUID(), 
                userId, 
                "user", 
                "email", 
                "email", 
                "Registration", 
                "Success", 
                null
            )
        );
    }

}