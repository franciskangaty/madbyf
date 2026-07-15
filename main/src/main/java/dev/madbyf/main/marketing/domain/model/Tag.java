package dev.madbyf.main.marketing.domain.model;

import java.util.UUID;

/**
 * Tag
 */
public record Tag(
    UUID id,
    String name,
    String description
) {

}
