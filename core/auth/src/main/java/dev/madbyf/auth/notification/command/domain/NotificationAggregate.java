package dev.madbyf.auth.notification.command.domain;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.util.Map;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import dev.madbyf.auth.notification.command.api.commands.NotificationCommand;
import dev.madbyf.auth.notification.command.domain.events.NotificationEvent;

@Aggregate
public class NotificationAggregate {

    // =========================
    // STATE
    // =========================

    @AggregateIdentifier
    private UUID notificationId;

    private UUID recipientId;
    private String recipientEntity;

    private String channel;

    private String type;
    private String title;
    private String message;

    private Map<String, String> metadata;

    private boolean sent;
    private boolean delivered;
    private boolean read;
    private boolean cancelled;

    private String deliveryFailureReason;
    private String cancellationReason;

    // =========================
    // CONSTRUCTOR
    // =========================

    /**
     * Required by Axon for event sourcing.
     */
    protected NotificationAggregate() {
    }

    /**
     * Creates the notification aggregate.
     */
    @CommandHandler
    public NotificationAggregate(NotificationCommand.Create command) {

        validateCreate(command);

        apply(
                new NotificationEvent.Created(
                        command.notificationId(),
                        command.recipientId(),
                        command.recipientEntity(),
                        command.channel(),
                        command.type(),
                        command.title(),
                        command.message(),
                        command.metadata()
                )
        );
    }

    // =========================
    // DELETE
    // =========================

    @CommandHandler
    public void handle(NotificationCommand.Delete command) {

        requireExists();
        requireNotCancelled();

        apply(
                new NotificationEvent.Deleted(
                        notificationId
                )
        );

        AggregateLifecycle.markDeleted();
    }

    // =========================
    // SEND
    // =========================

    @CommandHandler
    public void handle(NotificationCommand.Send command) {

        requireExists();
        requireNotCancelled();

        validateChannel(command.channel());

        if (sent) {
            throw new IllegalStateException(
                    "Notification has already been sent"
            );
        }

        apply(
                new NotificationEvent.Sent(
                        notificationId,
                        command.channel()
                )
        );
    }

    // =========================
    // DELIVERY SUCCESS
    // =========================

    @CommandHandler
    public void handle(
            NotificationCommand.MarkDeliverySucceeded command
    ) {

        requireExists();
        requireNotCancelled();

        validateChannel(command.channel());

        if (!sent) {
            throw new IllegalStateException(
                    "Notification has not been sent"
            );
        }

        if (delivered) {
            throw new IllegalStateException(
                    "Notification has already been delivered"
            );
        }

        apply(
                new NotificationEvent.DeliverySucceeded(
                        notificationId,
                        command.channel()
                )
        );
    }

    // =========================
    // DELIVERY FAILURE
    // =========================

    @CommandHandler
    public void handle(
            NotificationCommand.MarkDeliveryFailed command
    ) {

        requireExists();
        requireNotCancelled();

        validateChannel(command.channel());

        if (!sent) {
            throw new IllegalStateException(
                    "Notification has not been sent"
            );
        }

        if (delivered) {
            throw new IllegalStateException(
                    "Notification has already been delivered"
            );
        }

        if (command.reason() == null
                || command.reason().isBlank()) {
            throw new IllegalArgumentException(
                    "Delivery failure reason is required"
            );
        }

        apply(
                new NotificationEvent.DeliveryFailed(
                        notificationId,
                        command.channel(),
                        command.reason()
                )
        );
    }

    // =========================
    // READ
    // =========================

    @CommandHandler
    public void handle(NotificationCommand.MarkAsRead command) {

        requireExists();
        requireNotCancelled();

        if (read) {
            return;
        }

        apply(
                new NotificationEvent.Read(
                        notificationId
                )
        );
    }

    @CommandHandler
    public void handle(NotificationCommand.MarkAsUnread command) {

        requireExists();
        requireNotCancelled();

        if (!read) {
            return;
        }

        apply(
                new NotificationEvent.Unread(
                        notificationId
                )
        );
    }

    // =========================
    // CANCEL
    // =========================

    @CommandHandler
    public void handle(NotificationCommand.Cancel command) {

        requireExists();

        if (cancelled) {
            throw new IllegalStateException(
                    "Notification has already been cancelled"
            );
        }

        if (command.reason() == null
                || command.reason().isBlank()) {
            throw new IllegalArgumentException(
                    "Cancellation reason is required"
            );
        }

        apply(
                new NotificationEvent.Cancelled(
                        notificationId,
                        command.reason()
                )
        );
    }

    // =========================================================
    // EVENT SOURCING
    // =========================================================

    @EventSourcingHandler
    public void on(NotificationEvent.Created event) {

        this.notificationId = event.notificationId();

        this.recipientId = event.recipientId();
        this.recipientEntity = event.recipientEntity();

        this.channel = event.channel();

        this.type = event.type();
        this.title = event.title();
        this.message = event.message();

        this.metadata = event.metadata();

        this.sent = false;
        this.delivered = false;
        this.read = false;
        this.cancelled = false;
    }

    @EventSourcingHandler
    public void on(NotificationEvent.Sent event) {

        this.sent = true;
    }

    @EventSourcingHandler
    public void on(NotificationEvent.DeliverySucceeded event) {

        this.delivered = true;
        this.deliveryFailureReason = null;
    }

    @EventSourcingHandler
    public void on(NotificationEvent.DeliveryFailed event) {

        this.delivered = false;
        this.deliveryFailureReason = event.reason();
    }

    @EventSourcingHandler
    public void on(NotificationEvent.Read event) {

        this.read = true;
    }

    @EventSourcingHandler
    public void on(NotificationEvent.Unread event) {

        this.read = false;
    }

    @EventSourcingHandler
    public void on(NotificationEvent.Cancelled event) {

        this.cancelled = true;
        this.cancellationReason = event.reason();
    }

    @EventSourcingHandler
    public void on(NotificationEvent.Deleted event) {
        // Nothing to rebuild in aggregate state.
        // AggregateLifecycle.markDeleted() is handled by the command handler.
    }

    // =========================
    // VALIDATION
    // =========================

    private void requireExists() {

        if (notificationId == null) {
            throw new IllegalStateException(
                    "Notification does not exist"
            );
        }
    }

    private void requireNotCancelled() {

        if (cancelled) {
            throw new IllegalStateException(
                    "Notification has been cancelled"
            );
        }
    }

    private void validateCreate(
            NotificationCommand.Create command
    ) {

        if (command.notificationId() == null) {
            throw new IllegalArgumentException(
                    "Notification ID is required"
            );
        }

        if (command.recipientId() == null) {
            throw new IllegalArgumentException(
                    "Recipient ID is required"
            );
        }

        if (command.recipientEntity() == null
                || command.recipientEntity().isBlank()) {
            throw new IllegalArgumentException(
                    "Recipient entity is required"
            );
        }

        validateChannel(command.channel());

        if (command.type() == null
                || command.type().isBlank()) {
            throw new IllegalArgumentException(
                    "Notification type is required"
            );
        }

        if (command.message() == null
                || command.message().isBlank()) {
            throw new IllegalArgumentException(
                    "Notification message is required"
            );
        }
    }

    private void validateChannel(String channel) {

        if (channel == null || channel.isBlank()) {
            throw new IllegalArgumentException(
                    "Notification channel is required"
            );
        }
    }
}
