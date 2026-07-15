package dev.madbyf.main.institution.domain.model;

import java.time.Instant;
import java.util.UUID;

public record InstitutionBranch(
    UUID id,
    String name,
    Institution institution,
    Address address,
    Contact contact,
    Instant createdAt,
    String createdBy,
    Instant updatedAt,
    String updatedBy
) {
    
}
