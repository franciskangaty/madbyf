import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class AdvertisementCommands {

    public record CreateAdvertisement(
            @TargetAggregateIdentifier UUID advertisementId,
            UUID institutionId,
            String title,
            String advertisementType,
            String destinationUrl
    ) {}

    public record UpdateAdvertisement(
            @TargetAggregateIdentifier UUID advertisementId,
            String title,
            String destinationUrl
    ) {}

    public record ActivateAdvertisement(
            @TargetAggregateIdentifier UUID advertisementId
    ) {}

    public record PauseAdvertisement(
            @TargetAggregateIdentifier UUID advertisementId,
            String reason
    ) {}

    public record ResumeAdvertisement(
            @TargetAggregateIdentifier UUID advertisementId
    ) {}

    public record ArchiveAdvertisement(
            @TargetAggregateIdentifier UUID advertisementId
    ) {}

    public record DeleteAdvertisement(
            @TargetAggregateIdentifier UUID advertisementId
    ) {}
}