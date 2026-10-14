import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class PublicationTagCommands {

    public record CreateTag(
            @TargetAggregateIdentifier UUID tagId,
            UUID institutionId,
            String name,
            String color
    ) {}

    public record RenameTag(
            @TargetAggregateIdentifier UUID tagId,
            String name
    ) {}

    public record MergeTag(
            @TargetAggregateIdentifier UUID tagId,
            UUID targetTagId
    ) {}

    public record ArchiveTag(
            @TargetAggregateIdentifier UUID tagId
    ) {}

    public record DeleteTag(
            @TargetAggregateIdentifier UUID tagId
    ) {}
}