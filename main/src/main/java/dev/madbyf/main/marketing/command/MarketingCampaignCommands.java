import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class MarketingCampaignCommands {

    public record CreateCampaign(
            @TargetAggregateIdentifier UUID campaignId,
            UUID institutionId,
            String name,
            String code,
            String campaignType,
            String objective,
            String visibility,
            LocalDate startDate,
            LocalDate endDate,
            String targetAudience,
            BigDecimal budget
    ) {}

    public record UpdateCampaign(
            @TargetAggregateIdentifier UUID campaignId,
            String name,
            String objective,
            String visibility,
            LocalDate startDate,
            LocalDate endDate
    ) {}

    public record ScheduleCampaign(
            @TargetAggregateIdentifier UUID campaignId,
            Instant startAt,
            Instant endAt
    ) {}

    public record LaunchCampaign(
            @TargetAggregateIdentifier UUID campaignId
    ) {}

    public record PauseCampaign(
            @TargetAggregateIdentifier UUID campaignId,
            String reason
    ) {}

    public record ResumeCampaign(
            @TargetAggregateIdentifier UUID campaignId
    ) {}

    public record CompleteCampaign(
            @TargetAggregateIdentifier UUID campaignId
    ) {}

    public record CancelCampaign(
            @TargetAggregateIdentifier UUID campaignId,
            String reason
    ) {}

    public record ChangeCampaignAudience(
            @TargetAggregateIdentifier UUID campaignId,
            String targetAudience
    ) {}

    public record AdjustCampaignBudget(
            @TargetAggregateIdentifier UUID campaignId,
            BigDecimal budget
    ) {}

    public record DeleteCampaign(
            @TargetAggregateIdentifier UUID campaignId
    ) {}
}