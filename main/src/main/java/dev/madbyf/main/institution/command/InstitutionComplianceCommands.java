import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class InstitutionComplianceCommands {

    public record AddComplianceRequirement(
            @TargetAggregateIdentifier UUID institutionId,
            UUID complianceId,
            String requirement,
            String authority,
            LocalDate effectiveDate
    ) {}

    public record VerifyCompliance(
            @TargetAggregateIdentifier UUID institutionId,
            UUID complianceId
    ) {}

    public record ReportComplianceViolation(
            @TargetAggregateIdentifier UUID institutionId,
            UUID complianceId,
            String violationType,
            String description
    ) {}

    public record ResolveComplianceViolation(
            @TargetAggregateIdentifier UUID institutionId,
            UUID complianceId,
            String resolution
    ) {}

    public record RemoveComplianceRequirement(
            @TargetAggregateIdentifier UUID institutionId,
            UUID complianceId
    ) {}
}