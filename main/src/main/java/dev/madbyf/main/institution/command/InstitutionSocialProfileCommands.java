import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class InstitutionSocialProfileCommands {

    public record LinkSocialProfile(
            @TargetAggregateIdentifier UUID institutionId,
            String platform,
            String profileUrl
    ) {}

    public record UpdateSocialProfile(
            @TargetAggregateIdentifier UUID institutionId,
            String platform,
            String profileUrl
    ) {}

    public record RemoveSocialProfile(
            @TargetAggregateIdentifier UUID institutionId,
            String platform
    ) {}
}