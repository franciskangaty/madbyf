import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class InstitutionSettingsCommands {

    public record ConfigureSetting(
            @TargetAggregateIdentifier UUID institutionId,
            String key,
            String value
    ) {}

    public record ChangeSetting(
            @TargetAggregateIdentifier UUID institutionId,
            String key,
            String value
    ) {}

    public record ResetSetting(
            @TargetAggregateIdentifier UUID institutionId,
            String key
    ) {}
}