package dev.madbyf.main.legal.domain.model;

import java.time.Instant;
import java.util.UUID;

/**
 * LegalDocumentVersion
 */
public record LegalDocumentVersion(
    UUID id,
    String content,
    LegalDocument legalDocument,
    int version,
    boolean isLatest,
    Instant createdAt,
    Instant updatedAt
) {

}
