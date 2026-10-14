import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class PublicationCategoryCommands {

    public record CreateCategory(
            @TargetAggregateIdentifier UUID categoryId,
            UUID institutionId,
            String name,
            String description
    ) {}

    public record RenameCategory(
            @TargetAggregateIdentifier UUID categoryId,
            String name
    ) {}

    public record ChangeCategoryVisibility(
            @TargetAggregateIdentifier UUID categoryId,
            String visibility
    ) {}

    public record ArchiveCategory(
            @TargetAggregateIdentifier UUID categoryId
    ) {}

    public record RestoreCategory(
            @TargetAggregateIdentifier UUID categoryId
    ) {}

    public record DeleteCategory(
            @TargetAggregateIdentifier UUID categoryId
    ) {}
}