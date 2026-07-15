package dev.madbyf.main.marketing.domain.model;

import java.util.UUID;

/**
 * BlogType
 */
public record BlogType(
    UUID id,
    String name,
    String description
) {

}
