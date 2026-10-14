import java.time.Instant;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class NewsletterCommands {

    public record CreateNewsletter(
            @TargetAggregateIdentifier UUID newsletterId,
            UUID institutionId,
            String title,
            String subject
    ) {}

    public record UpdateNewsletter(
            @TargetAggregateIdentifier UUID newsletterId,
            String title,
            String subject
    ) {}

    public record DefineAudience(
            @TargetAggregateIdentifier UUID newsletterId,
            String audienceType,
            String audienceCriteria
    ) {}

    public record ScheduleNewsletter(
            @TargetAggregateIdentifier UUID newsletterId,
            Instant scheduledAt
    ) {}

    public record SendNewsletter(
            @TargetAggregateIdentifier UUID newsletterId
    ) {}

    public record CancelNewsletter(
            @TargetAggregateIdentifier UUID newsletterId,
            String reason
    ) {}

    public record DeleteNewsletter(
            @TargetAggregateIdentifier UUID newsletterId
    ) {}
}