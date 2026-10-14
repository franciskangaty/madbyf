import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class InstitutionBrandingCommands {

    public record UploadInstitutionLogo(
            @TargetAggregateIdentifier UUID institutionId,
            UUID logoId,
            String logoUrl
    ) {}

    public record RemoveInstitutionLogo(
            @TargetAggregateIdentifier UUID institutionId
    ) {}

    public record UploadInstitutionBanner(
            @TargetAggregateIdentifier UUID institutionId,
            UUID bannerId,
            String bannerUrl
    ) {}

    public record RemoveInstitutionBanner(
            @TargetAggregateIdentifier UUID institutionId
    ) {}

    public record UpdateBrandColors(
            @TargetAggregateIdentifier UUID institutionId,
            String primaryColor,
            String secondaryColor,
            String accentColor
    ) {}

    public record ChangeInstitutionTheme(
            @TargetAggregateIdentifier UUID institutionId,
            String theme
    ) {}

    public record UpdateInstitutionFavicon(
            @TargetAggregateIdentifier UUID institutionId,
            UUID faviconId
    ) {}
}