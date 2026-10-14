import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class InstitutionAccreditationCommands {

    public record AddAccreditation(
            @TargetAggregateIdentifier UUID institutionId,
            UUID accreditationId,
            String authority,
            String accreditationNumber,
            LocalDate issuedDate,
            LocalDate expiryDate
    ) {}

    public record RenewAccreditation(
            @TargetAggregateIdentifier UUID institutionId,
            UUID accreditationId,
            String renewalNumber,
            LocalDate renewedUntil
    ) {}

    public record RevokeAccreditation(
            @TargetAggregateIdentifier UUID institutionId,
            UUID accreditationId,
            String authority,
            String reason
    ) {}

    public record RemoveAccreditation(
            @TargetAggregateIdentifier UUID institutionId,
            UUID accreditationId
    ) {}
}