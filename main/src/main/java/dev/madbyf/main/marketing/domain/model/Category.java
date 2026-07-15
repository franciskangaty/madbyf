package dev.madbyf.main.marketing.domain.model;

import java.util.UUID;

/**
 * Category
 */
public record Category(
    UUID id,
    String name,
    String description
) {

}
