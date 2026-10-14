package dev.madbyf.auth.common.command.domain;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;

import dev.madbyf.auth.common.command.api.commands.UsernameAggregateCommand;
import dev.madbyf.auth.common.command.domain.events.UsernameAggregateEvent;

@Aggregate
public class UsernameAggregate {

    // =========================
    // STATE
    // =========================

    @AggregateIdentifier
    private String username;

    private boolean reserved;

    private boolean assigned;

    // =========================
    // CONSTRUCTOR
    // =========================

    /**
     * Required by Axon for event sourcing.
     */
    protected UsernameAggregate() {
    }

    // =========================

    @CommandHandler 
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public void handle(UsernameAggregateCommand.Reserve command) {
        if (reserved) {
            throw new IllegalStateException("Username is already reserved");
        }
        if (assigned) {
            throw new IllegalStateException("Username is already assigned");
        }
        apply(new UsernameAggregateEvent.Reserved(command.username(), command.ownerRef()));
    }

    @CommandHandler 
    public void handle(UsernameAggregateCommand.MoveReservation command) {
        if (!reserved) {
            throw new IllegalStateException("Username is not reserved");
        }
        if (assigned) {
            throw new IllegalStateException("Username is already assigned");
        }
        apply(new UsernameAggregateEvent.ReservationMoved(command.username(), command.ownerRef()));
    }

    @CommandHandler 
    public void handle(UsernameAggregateCommand.CancelReservation command) {
        if (!reserved) {
            throw new IllegalStateException("Username is not reserved");
        }
        if (assigned) {
            throw new IllegalStateException("Username is already assigned");
        }
        apply(new UsernameAggregateEvent.ReservationCancelled(command.username(), command.reason()));
    }

    @CommandHandler 
    public void handle(UsernameAggregateCommand.ExpireReservation command) {
        if (!reserved) {
            throw new IllegalStateException("Username is not reserved");
        }
        if (assigned) {
            throw new IllegalStateException("Username is already assigned");
        }
        apply(new UsernameAggregateEvent.ReservationExpired(command.username()));
    }

    @CommandHandler 
    public void handle(UsernameAggregateCommand.Assign command) {
        if (!reserved) {
            throw new IllegalStateException("Username is not reserved");
        }
        if (assigned) {
            throw new IllegalStateException("Username is already assigned");
        }
        apply(new UsernameAggregateEvent.Assigned(command.username()));
    }

    @CommandHandler 
    public void handle(UsernameAggregateCommand.Release command) {
        if (!reserved) {
            throw new IllegalStateException("Username is not reserved");
        }
        if (!assigned) {
            throw new IllegalStateException("Username is not assigned");
        }
        apply(new UsernameAggregateEvent.Released(command.username(), command.reason()));
    }

    // =========================
    // EVENT HANDLERS
    // =========================

    @EventSourcingHandler
    public void on(UsernameAggregateEvent.Reserved event) {
        this.username = event.username();
        this.reserved = true;
    }

    @EventSourcingHandler
    public void on(UsernameAggregateEvent.ReservationMoved event) {
        // No state change needed for moving reservation, but could log or track ownerRef if desired
    }

    @EventSourcingHandler
    public void on(UsernameAggregateEvent.ReservationCancelled event) {
        this.reserved = false;
    }

    @EventSourcingHandler
    public void on(UsernameAggregateEvent.ReservationExpired event) {
        this.reserved = false;
    }

    @EventSourcingHandler
    public void on(UsernameAggregateEvent.Assigned event) {
        this.reserved = false; // Once assigned, it's no longer just reserved
        this.assigned = true;
    }

    @EventSourcingHandler
    public void on(UsernameAggregateEvent.Released event) {
        this.reserved = false; // Once released, it's no longer reserved
        this.assigned = false;
    }
}
