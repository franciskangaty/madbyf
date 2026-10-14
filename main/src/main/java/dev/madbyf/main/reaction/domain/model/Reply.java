package dev.madbyf.main.reaction.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Reply(
    UUID id,
    UUID commentId,
    UUID authorId,
    String text,
    Instant createdAt,
    Instant updatedAt
) {
}