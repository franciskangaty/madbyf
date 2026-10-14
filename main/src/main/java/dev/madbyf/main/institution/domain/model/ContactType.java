package dev.madbyf.main.institution.domain.model;

import java.util.UUID;

/**
 * ContactType
 */
public record ContactType(
    UUID id,
    String name,
    String description
) {

}
