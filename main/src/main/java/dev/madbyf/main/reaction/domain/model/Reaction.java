package dev.madbyf.main.reaction.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Reaction(
    UUID id,
    UUID resourceId,
    String resourceType,
    UUID authorId,
    Instant createdAt
) {
}