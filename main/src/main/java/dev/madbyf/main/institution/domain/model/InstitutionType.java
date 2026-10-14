package dev.madbyf.main.institution.domain.model;

import java.util.UUID;

/**
 * InstitutionType
 */
public record InstitutionType(
    UUID id,
    String name,
    String description
) {

}
