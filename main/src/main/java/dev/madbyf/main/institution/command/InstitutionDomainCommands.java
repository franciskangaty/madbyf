import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class InstitutionDomainCommands {

    public record RegisterDomain(
            @TargetAggregateIdentifier UUID institutionId,
            UUID domainId,
            String domain
    ) {}

    public record VerifyDomainOwnership(
            @TargetAggregateIdentifier UUID institutionId,
            UUID domainId,
            String verificationMethod
    ) {}

    public record SetPrimaryDomain(
            @TargetAggregateIdentifier UUID institutionId,
            UUID domainId
    ) {}

    public record RemoveDomain(
            @TargetAggregateIdentifier UUID institutionId,
            UUID domainId
    ) {}
}