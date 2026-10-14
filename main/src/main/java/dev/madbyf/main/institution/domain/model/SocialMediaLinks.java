package dev.madbyf.main.institution.domain.model;

import java.util.UUID;

/**
 * SocialMediaLinks
 */
public record SocialMediaLinks(
    UUID id,
    Platform platform,
    String url
) {

}
