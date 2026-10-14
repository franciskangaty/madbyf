import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class MediaLibraryCommands {

    public record UploadMedia(
            @TargetAggregateIdentifier UUID mediaId,
            UUID institutionId,
            String fileName,
            String fileType,
            String mimeType,
            Long size
    ) {}

    public record CategorizeMedia(
            @TargetAggregateIdentifier UUID mediaId,
            String category
    ) {}

    public record OptimizeMedia(
            @TargetAggregateIdentifier UUID mediaId,
            String optimizationType
    ) {}

    public record ReplaceMedia(
            @TargetAggregateIdentifier UUID mediaId,
            String fileName
    ) {}

    public record ArchiveMedia(
            @TargetAggregateIdentifier UUID mediaId
    ) {}

    public record DeleteMedia(
            @TargetAggregateIdentifier UUID mediaId
    ) {}
}