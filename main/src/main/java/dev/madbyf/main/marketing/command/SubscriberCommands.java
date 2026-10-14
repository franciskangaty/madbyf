import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class SubscriberCommands {

    public record RegisterSubscriber(
            @TargetAggregateIdentifier UUID subscriberId,
            UUID institutionId,
            String email,
            String fullName,
            String source
    ) {}

    public record VerifySubscriber(
            @TargetAggregateIdentifier UUID subscriberId
    ) {}

    public record UpdateSubscriberPreferences(
            @TargetAggregateIdentifier UUID subscriberId,
            String preferences
    ) {}

    public record Unsubscribe(
            @TargetAggregateIdentifier UUID subscriberId,
            String reason
    ) {}

    public record Resubscribe(
            @TargetAggregateIdentifier UUID subscriberId
    ) {}

    public record DeleteSubscriber(
            @TargetAggregateIdentifier UUID subscriberId
    ) {}
}