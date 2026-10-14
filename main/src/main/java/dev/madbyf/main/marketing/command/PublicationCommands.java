import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class PublicationCommands {

    // ==================================== CRUD ======================================

    public record CreatePublication(
            @TargetAggregateIdentifier UUID publicationId,
            UUID institutionId,
            String publicationType,
            String title,
            String slug,
            UUID categoryId,
            UUID authorId,
            String summary,
            String content,
            String visibility
    ) {}

    public record UpdatePublication(
            @TargetAggregateIdentifier UUID publicationId,
            String title,
            String summary,
            String content
    ) {}

    public record DeletePublication(
            @TargetAggregateIdentifier UUID publicationId
    ) {}

    // ==================================== CONTENT ======================================

    public record AssignCategory(
            @TargetAggregateIdentifier UUID publicationId,
            UUID categoryId
    ) {}

    public record AssignTags(
            @TargetAggregateIdentifier UUID publicationId,
            List<UUID> tagIds
    ) {}

    public record ChangeCoverImage(
            @TargetAggregateIdentifier UUID publicationId,
            UUID mediaId
    ) {}

    public record TranslatePublication(
            @TargetAggregateIdentifier UUID publicationId,
            String language
    ) {}

    // ==================================== LIFECYCLE ======================================

    public record SchedulePublication(
            @TargetAggregateIdentifier UUID publicationId,
            Instant publishAt
    ) {}

    public record PublishPublication(
            @TargetAggregateIdentifier UUID publicationId
    ) {}

    public record UnpublishPublication(
            @TargetAggregateIdentifier UUID publicationId,
            String reason
    ) {}

    public record FeaturePublication(
            @TargetAggregateIdentifier UUID publicationId,
            Instant featuredUntil
    ) {}

    public record UnfeaturePublication(
            @TargetAggregateIdentifier UUID publicationId
    ) {}

    public record PinPublication(
            @TargetAggregateIdentifier UUID publicationId,
            Instant pinnedUntil
    ) {}

    public record UnpinPublication(
            @TargetAggregateIdentifier UUID publicationId
    ) {}

    public record ChangePublicationVisibility(
            @TargetAggregateIdentifier UUID publicationId,
            String visibility
    ) {}

    public record ArchivePublication(
            @TargetAggregateIdentifier UUID publicationId,
            String reason
    ) {}

    public record RestorePublication(
            @TargetAggregateIdentifier UUID publicationId
    ) {}
}