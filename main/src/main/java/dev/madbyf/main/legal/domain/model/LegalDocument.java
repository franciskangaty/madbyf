package dev.madbyf.main.legal.domain.model;

import java.time.Instant;
import java.util.UUID;

public record LegalDocument(
    UUID id,
    String institutionId,
    LegalDocumentType type,
    String title,
    String description,
    UUID authorId,
    int views,
    LegalDocumentStatus status,
    LegalDocumentVersion version,
    Instant createdAt,
    Instant updatedAt
) {
    
}
