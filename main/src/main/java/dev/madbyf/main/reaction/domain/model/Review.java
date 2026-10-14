package dev.madbyf.main.reaction.domain.model;

import java.util.UUID;

public record Review(
    UUID id,
    UUID resourceId,
    String resourceType,
    UUID reviewerId,
    String text, 
    String rating
) {
}