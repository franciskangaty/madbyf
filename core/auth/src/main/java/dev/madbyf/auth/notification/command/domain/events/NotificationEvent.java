package dev.madbyf.auth.notification.command.domain.events;

import java.util.Map;
import java.util.UUID;

public final class NotificationEvent {

    private NotificationEvent() {
    }

    // =========================
    // LIFECYCLE
    // =========================

    public record Created(
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

    public record Deleted(
            UUID notificationId
    ) {
    }

    // =========================
    // DELIVERY
    // =========================

    public record Sent(
            UUID notificationId,
            String channel
    ) {
    }

    public record DeliverySucceeded(
            UUID notificationId,
            String channel
    ) {
    }

    public record DeliveryFailed(
            UUID notificationId,
            String channel,
            String reason
    ) {
    }

    // =========================
    // READ STATE
    // =========================

    public record Read(
            UUID notificationId
    ) {
    }

    public record Unread(
            UUID notificationId
    ) {
    }

    // =========================
    // CANCELLATION
    // =========================

    public record Cancelled(
            UUID notificationId,
            String reason
    ) {
    }
}
