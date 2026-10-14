package dev.madbyf.auth.user.command.domain;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import dev.madbyf.auth.user.command.api.commands.UserProfileCommand;
import dev.madbyf.auth.user.command.domain.events.UserProfileEvent;

@Aggregate
public class UserProfileAggregate {

    // =========================
    // STATE
    // =========================

    @AggregateIdentifier
    private UUID userProfileId;

    private String displayName;
    private String firstName;
    private String middleName;
    private String lastName;

    private boolean active;
    private String visibility;

    // =========================
    // CONSTRUCTORS
    // =========================

    /**
     * Required by Axon for event sourcing.
     */
    protected UserProfileAggregate() {
    }

    /**
     * Aggregate creation.
     */
    @CommandHandler
    public UserProfileAggregate(UserProfileCommand.Create command) {

        if (command.userProfileId() == null) {
            throw new IllegalArgumentException(
                    "User profile ID is required"
            );
        }

        if (command.userAccountId() == null) {
            throw new IllegalArgumentException(
                    "User account ID is required"
            );
        }

        if (command.displayName() == null
                || command.displayName().isBlank()) {
            throw new IllegalArgumentException(
                    "Display name is required"
            );
        }

        apply(
                new UserProfileEvent.Created(
                        command.userProfileId(),
                        command.userAccountId(),
                        command.displayName(),
                        command.firstName(),
                        command.middleName(),
                        command.lastName()
                )
        );
    }

    // =========================
    // CRUD
    // =========================

    @CommandHandler
    public void handle(UserProfileCommand.Update command) {

        requireExists();

        apply(
                new UserProfileEvent.Updated(
                        userProfileId,

                        displayName,
                        command.displayName(),

                        firstName,
                        command.firstName(),

                        middleName,
                        command.middleName(),

                        lastName,
                        command.lastName()
                )
        );
    }

    @CommandHandler
    public void handle(UserProfileCommand.Delete command) {

        requireExists();

        apply(
                new UserProfileEvent.Deleted(
                        userProfileId
                )
        );

        AggregateLifecycle.markDeleted();
    }

    // =========================
    // STATUS
    // =========================

    @CommandHandler
    public void handle(UserProfileCommand.Activate command) {

        requireExists();

        if (active) {
            throw new IllegalStateException(
                    "User profile is already active"
            );
        }

        apply(
                new UserProfileEvent.Activated(
                        userProfileId
                )
        );
    }

    @CommandHandler
    public void handle(UserProfileCommand.Deactivate command) {

        requireExists();

        if (!active) {
            throw new IllegalStateException(
                    "User profile is already inactive"
            );
        }

        apply(
                new UserProfileEvent.Deactivated(
                        userProfileId
                )
        );
    }

    @CommandHandler
    public void handle(UserProfileCommand.ChangeVisibility command) {

        requireExists();

        if (command.visibility() == null
                || command.visibility().isBlank()) {
            throw new IllegalArgumentException(
                    "Visibility is required"
            );
        }

        if (command.visibility().equals(visibility)) {
            return;
        }

        apply(
                new UserProfileEvent.VisibilityChanged(
                        userProfileId,
                        visibility,
                        command.visibility()
                )
        );
    }

    // =========================================================
    // EVENT SOURCING
    // =========================================================

    @EventSourcingHandler
    public void on(UserProfileEvent.Created event) {

        this.userProfileId = event.userProfileId();

        this.displayName = event.displayName();
        this.firstName = event.firstName();
        this.middleName = event.middleName();
        this.lastName = event.lastName();

        this.active = false;

        // Choose whatever your domain default is.
        this.visibility = "PRIVATE";
    }

    @EventSourcingHandler
    public void on(UserProfileEvent.Updated event) {

        this.displayName = event.newDisplayName();
        this.firstName = event.newFirstName();
        this.middleName = event.newMiddleName();
        this.lastName = event.newLastName();
    }

    @EventSourcingHandler
    public void on(UserProfileEvent.Activated event) {
        this.active = true;
    }

    @EventSourcingHandler
    public void on(UserProfileEvent.Deactivated event) {
        this.active = false;
    }

    @EventSourcingHandler
    public void on(UserProfileEvent.VisibilityChanged event) {
        this.visibility = event.newVisibility();
    }

    @EventSourcingHandler
    public void on(UserProfileEvent.Deleted event) {
        // No state mutation required.
        // AggregateLifecycle.markDeleted() is called by the command handler.
    }

    // =========================
    // VALIDATION
    // =========================

    private void requireExists() {

        if (userProfileId == null) {
            throw new IllegalStateException(
                    "User profile does not exist"
            );
        }
    }
}
