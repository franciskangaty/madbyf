package dev.madbyf.main.marketing.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Faq(
   UUID id,
   String question,
   String answer,
   UUID authorId,
   int views,
   Instant createdAt,
   Instant updatedAt
) {
}