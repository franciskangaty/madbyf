package dev.madbyf.auth.notification.command.api.commands;

import java.util.Map;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class NotificationCommand {

    // =========================
    // LIFECYCLE
    // =========================

    public record Create(
            @TargetAggregateIdentifier
            UUID notificationId,

            UUID recipientId,
            String recipientEntity,

            String channel,

            String type,
            String title,
            String message,

            Map<String, String> metadata
    ) {
    }

    public record Delete(
            @TargetAggregateIdentifier
            UUID notificationId
    ) {
    }

    // =========================
    // DELIVERY
    // =========================

    public record MarkAsDelivered(
            @TargetAggregateIdentifier
            UUID notificationId
    ) {
    }

    public record MarkAsFailed(
            @TargetAggregateIdentifier
            UUID notificationId,

            String reason
    ) {
    }

        public record Send(
                @TargetAggregateIdentifier
                UUID notificationId,
                String channel
        ) {
        }

        public record MarkDeliverySucceeded(
                @TargetAggregateIdentifier
                UUID notificationId,
                String channel
        ) {
        }

        public record MarkDeliveryFailed(
                @TargetAggregateIdentifier
                UUID notificationId,
                String channel,
                String reason
        ) {
        }

    // =========================
    // READ STATE
    // =========================

    public record MarkAsRead(
            @TargetAggregateIdentifier
            UUID notificationId
    ) {
    }

    public record MarkAsUnread(
            @TargetAggregateIdentifier
            UUID notificationId
    ) {
    }

    // =========================
    // CANCELLATION
    // =========================

    public record Cancel(
            @TargetAggregateIdentifier
            UUID notificationId,

            String reason
    ) {
    }
}
