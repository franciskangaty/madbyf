import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class InstitutionSubscriptionCommands {

    public record AssignSubscription(
            @TargetAggregateIdentifier UUID institutionId,
            UUID subscriptionId,
            UUID planId,
            LocalDate startDate,
            LocalDate endDate
    ) {}

    public record UpgradeSubscription(
            @TargetAggregateIdentifier UUID institutionId,
            UUID newPlanId,
            LocalDate effectiveDate
    ) {}

    public record DowngradeSubscription(
            @TargetAggregateIdentifier UUID institutionId,
            UUID newPlanId,
            LocalDate effectiveDate
    ) {}

    public record RenewSubscription(
            @TargetAggregateIdentifier UUID institutionId,
            UUID subscriptionId,
            String renewalPeriod,
            LocalDate renewedUntil
    ) {}

    public record CancelSubscription(
            @TargetAggregateIdentifier UUID institutionId,
            UUID subscriptionId,
            String cancellationReason
    ) {}

    public record StartSubscriptionGracePeriod(
            @TargetAggregateIdentifier UUID institutionId,
            UUID subscriptionId,
            LocalDate gracePeriodEnd
    ) {}

    public record TerminateSubscription(
            @TargetAggregateIdentifier UUID institutionId,
            UUID subscriptionId,
            String terminationReason
    ) {}
}