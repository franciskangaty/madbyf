package dev.madbyf.auth.common.command.domain;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;

import dev.madbyf.auth.common.command.api.commands.ContactAggregateCommand;
import dev.madbyf.auth.common.command.domain.events.ContactAggregateEvent;
import dev.madbyf.auth.common.util.OwnerRef;

@Aggregate
public class ContactAggregate {

    // =========================
    // STATE
    // =========================

    @AggregateIdentifier
    private String contact;

    private Map<OwnerRef, Boolean> ownersWithVerificationStatus = new HashMap<>();
    private boolean removed;

    // =========================
    // CONSTRUCTOR
    // =========================

    /**
     * Required by Axon.
     */
    protected ContactAggregate() {
    }

    /**
     * Creates a new contact.
     */
    @CommandHandler
    public ContactAggregate(ContactAggregateCommand.Add command) {
        apply(new ContactAggregateEvent.Added(command.contact(), command.type()));
    }

    @CommandHandler
    public ContactAggregate(ContactAggregateCommand.AddWithOwner command) {
        apply(new ContactAggregateEvent.AddedWithOwner(command.contact(), command.type(), command.ownerRef()));
    }

    @CommandHandler 
    public void handle(ContactAggregateCommand.Remove command) {
        if (!ownersWithVerificationStatus.isEmpty()) {
            throw new IllegalStateException("Cannot remove contact with linked owners");
        }

        apply(new ContactAggregateEvent.Removed(command.contact()));
    }

    @CommandHandler
    public void handle(ContactAggregateCommand.LinkOwner command) {
        if (ownersWithVerificationStatus.containsKey(command.ownerRef())) {
            throw new IllegalStateException("Owner already linked to contact");
        }

        apply(new ContactAggregateEvent.OwnerLinked(command.contact(), command.ownerRef()));
    }
    
    @CommandHandler 
    public void handle(ContactAggregateCommand.UnlinkOwner command) {
        if (!ownersWithVerificationStatus.containsKey(command.ownerRef())) {
            throw new IllegalStateException("Owner not linked to contact");
        }

        apply(new ContactAggregateEvent.OwnerUnlinked(command.contact(), command.ownerRef()));
    }

    @CommandHandler
    public void handle(ContactAggregateCommand.RequestVerification command) {
        if (!ownersWithVerificationStatus.containsKey(command.ownerRef())) {
            throw new IllegalStateException("Owner not linked to contact");
        }
        // Here you would trigger the OTP generation workflow, but for now we just mark it as requested.
        apply(new ContactAggregateEvent.VerificationRequested(command.contact(), command.ownerRef()));
    }

    @CommandHandler
    public void handle(ContactAggregateCommand.Verify command) {
        if (!ownersWithVerificationStatus.containsKey(command.ownerRef())) {
            throw new IllegalStateException("Owner not linked to contact");
        }

        apply(new ContactAggregateEvent.Verified(command.contact(), command.ownerRef()));
    }

    // =========================
    // EVENT SOURCING HANDLERS
    // =========================

    @EventSourcingHandler
    public void on(ContactAggregateEvent.Added event) {
        this.contact = event.contact();
        this.removed = false;
    }

    @EventSourcingHandler
    public void on(ContactAggregateEvent.AddedWithOwner event) {
        this.contact = event.contact();
        this.ownersWithVerificationStatus.put(event.ownerRef(), false);
        this.removed = false;
    }

    @EventSourcingHandler
    public void on(ContactAggregateEvent.Removed event) {
        this.removed = true;
    }

    @EventSourcingHandler
    public void on(ContactAggregateEvent.OwnerLinked event) {
        this.ownersWithVerificationStatus.put(event.ownerRef(), false);
    }

    @EventSourcingHandler
    public void on(ContactAggregateEvent.OwnerUnlinked event) {
        this.ownersWithVerificationStatus.remove(event.ownerRef());
    }

    @EventSourcingHandler
    public void on(ContactAggregateEvent.VerificationRequested event) {
        // No state change needed for this event in the aggregate, but you might want to log or handle it.
    }

    @EventSourcingHandler
    public void on(ContactAggregateEvent.Verified event) {
        this.ownersWithVerificationStatus.put(event.ownerRef(), true);
    }

}


