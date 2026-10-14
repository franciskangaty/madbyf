package dev.madbyf.auth.common.command.domain.events;

import java.util.UUID;

public class UsernameEvent {

    public record Reserved(
        String username,
        String entity,
        UUID entityId
    ) {
    }

    public record Released(String username) {
    }

    public record ReservationFailed(String username, String reason) {
    }

    public record ReleaseFailed(String username, String reason) {
    }
}