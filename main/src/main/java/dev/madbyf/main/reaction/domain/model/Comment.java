package dev.madbyf.main.reaction.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Comment(
    UUID id,
    UUID resourceId,
    String resourceType,
    UUID authorId,
    String text,
    Instant createdAt,
    Instant updatedAt
) {
}