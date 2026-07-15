package dev.madbyf.main.marketing.domain.model;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record Blog(
        UUID id,
        BlogType type,
        String status,
        String title,
        String content,
        UUID authorId,
        Instant publishedAt,
        String thumbnailUrl,
        Set<Tag> tags,
        Set<Category> categories,
        boolean isFeatured,
        String slug
) { // type news, announcement, article; categories: news, events, updates, tips, tutorials, etc.
}