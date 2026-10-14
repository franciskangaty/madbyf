package dev.madbyf.main.institution.domain.model;

import java.util.UUID;

/**
 * Contact
 */
public record Contact(
    UUID id,
    String name,
    ContactType type,
    String value
) {
}
