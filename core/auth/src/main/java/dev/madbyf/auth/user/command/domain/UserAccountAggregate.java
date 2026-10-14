package dev.madbyf.auth.user.command.domain;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import dev.madbyf.auth.user.command.api.commands.UserAccountAggregateCommand;
import dev.madbyf.auth.user.command.domain.events.UserAccountAggregateEvent;

@Aggregate
public class UserAccountAggregate {

    @AggregateIdentifier
    private UUID userId;

    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;

    /**
     * Store a password hash, never a plaintext password.
     */
    private String passwordHash;

    private boolean active;
    private boolean suspended;
    private boolean credentialExpired;
    private boolean accountExpired;

    private boolean locked;

    // Required by Axon
    protected UserAccountAggregate() {
    }

    // =========================
    // CREATE
    // =========================

    @CommandHandler
    public UserAccountAggregate(UserAccountAggregateCommand.Create command) {
        apply(new UserAccountAggregateEvent.Created(
            command.userId(), 
            command.firstName(), 
            command.middleName(), 
            command.lastName(), 
            command.dateOfBirth(), 
            command.passwordHash()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.UpdateNames command) {
        if (firstName.equals(command.firstName()) && middleName.equals(command.middleName()) && lastName.equals(command.lastName())) {
            throw new IllegalArgumentException("No changes detected in names");
        }
        if (!active) {
            throw new IllegalStateException("Cannot update names of an inactive account");
        }
        if (locked) {
            throw new IllegalStateException("Cannot update names of a locked account");
        }
        apply(new UserAccountAggregateEvent.NamesUpdated(
            command.userId(), 
            firstName,
            command.firstName(), 
            middleName,
            command.middleName(), 
            lastName,
            command.lastName()
        ));
    }

    @CommandHandler
    public void handle(UserAccountAggregateCommand.UpdateDateOfBirth command) {
        if (dateOfBirth.equals(command.dateOfBirth())) {
            throw new IllegalArgumentException("No changes detected in date of birth");
        }
        if (!active) {
            throw new IllegalStateException("Cannot update date of birth of an inactive account");
        }
        if (locked) {
            throw new IllegalStateException("Cannot update date of birth of a locked account");
        }
        apply(new UserAccountAggregateEvent.DateOfBirthUpdated(
            command.userId(),
            dateOfBirth,
            command.dateOfBirth()
        ));
    }

    @CommandHandler
    public void handle(UserAccountAggregateCommand.UpdatePassword command) {
        if (!passwordHash.equals(command.oldPasswordHash())) {
            throw new IllegalArgumentException("Old password hash does not match input password hash");
        }
        if (passwordHash.equals(command.newPasswordHash())) {
            throw new IllegalArgumentException("New password hash must be different from the old password hash");
        }
        if (!active) {
            throw new IllegalStateException("Cannot update password of an inactive account");
        }
        if (locked) {
            throw new IllegalStateException("Cannot update password of a locked account");
        }
        apply(new UserAccountAggregateEvent.PasswordUpdated(
            command.userId(),
            command.newPasswordHash()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.SetProfilePhotoUrl command) {
        if (!active) {
            throw new IllegalStateException("Cannot set profile photo URL of an inactive account");
        }
        if (locked) {
            throw new IllegalStateException("Cannot set profile photo URL of a locked account");
        }
        apply(new UserAccountAggregateEvent.ProfilePhotoUrlSet(
            command.userId(),
            command.profilePhotoUrl()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.VerifyName command) {
        apply(new UserAccountAggregateEvent.NameVerified(
            command.userId(),
            command.verificationMethod()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.VerifyAge command) {
        apply(new UserAccountAggregateEvent.AgeVerified(
            command.userId(),
            command.verificationMethod()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.SetPassword command) {
        if (!active) {
            throw new IllegalStateException("Cannot set password of an inactive account");
        }
        if (locked) {
            throw new IllegalStateException("Cannot set password of a locked account");
        }
        apply(new UserAccountAggregateEvent.PasswordSet(
            command.userId()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.RemovePassword command) {
        if (!active) {
            throw new IllegalStateException("Cannot remove password of an inactive account");
        }
        if (locked) {
            throw new IllegalStateException("Cannot remove password of a locked account");
        }
        apply(new UserAccountAggregateEvent.PasswordRemoved(
            command.userId()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.Enable command) {
        apply(new UserAccountAggregateEvent.Enabled(
            command.userId()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.Disable command) {
        if (!active) {
            throw new IllegalStateException("Account is already disabled");
        }
        apply(new UserAccountAggregateEvent.Disabled(
            command.userId(),
            command.reason()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.Lock command) {
        apply(new UserAccountAggregateEvent.Locked(
            command.userId(),
            command.reason()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.Unlock command) {
        apply(new UserAccountAggregateEvent.Unlocked(
            command.userId()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.ExpireCredentials command) {
        apply(new UserAccountAggregateEvent.CredentialsExpired(
            command.userId()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.RenewCredentials command) {
        apply(new UserAccountAggregateEvent.CredentialsRenewed(
            command.userId()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.ExpireAccount command) {
        apply(new UserAccountAggregateEvent.AccountExpired(
            command.userId()
        ));
    }

    @CommandHandler 
    public void handle(UserAccountAggregateCommand.ReactivateAccount command) {
        apply(new UserAccountAggregateEvent.AccountReactivated(
            command.userId()
        ));
    }

    // =========================
    // EVENT SOURCING HANDLERS
    // =========================

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.Created event) {
        this.userId = event.userId();
        this.firstName = event.firstName();
        this.middleName = event.middleName();
        this.lastName = event.lastName();
        this.dateOfBirth = event.dateOfBirth();
        this.passwordHash = event.passwordHash();
        this.active = true;
        this.suspended = false;
        this.locked = false;
        this.credentialExpired = false;
        this.accountExpired = false;
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.NamesUpdated event) {
        this.firstName = event.newFirstName();
        this.middleName = event.newMiddleName();
        this.lastName = event.newLastName();
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.DateOfBirthUpdated event) {
        this.dateOfBirth = event.newDateOfBirth();
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.PasswordUpdated event) {
        this.passwordHash = event.newPasswordHash();
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.ProfilePhotoUrlSet event) {
        // No state to update in this aggregate for profile photo URL, as it's likely stored elsewhere
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.NameVerified event) {
        // Handle name verification logic if needed
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.AgeVerified event) {
        // Handle age verification logic if needed
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.PasswordSet event) {
        // Handle password set logic if needed
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.PasswordRemoved event) {
        this.passwordHash = null;
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.Enabled event) {
        this.active = true;
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.Disabled event) {
        this.active = false;
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.Locked event) {
        this.locked = true;
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.Unlocked event) {
        this.locked = false;
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.CredentialsExpired event) {
        this.credentialExpired = true;
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.CredentialsRenewed event) {
        this.credentialExpired = false;
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.AccountExpired event) {
        this.accountExpired = true;
    }

    @EventSourcingHandler
    public void on(UserAccountAggregateEvent.AccountReactivated event) {
        this.accountExpired = false;
    }
}
