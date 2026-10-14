import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public final class InstitutionBranchCommands {

    public record EstablishBranch(
            @TargetAggregateIdentifier UUID institutionId,
            UUID branchId,
            String branchCode,
            String branchName,
            String address,
            String country,
            String region,
            String city,
            String phone,
            String email,
            LocalDate establishedDate
    ) {}

    public record RenameBranch(
            @TargetAggregateIdentifier UUID institutionId,
            UUID branchId,
            String newName
    ) {}

    public record RelocateBranch(
            @TargetAggregateIdentifier UUID institutionId,
            UUID branchId,
            String country,
            String region,
            String city,
            String address
    ) {}

    public record ActivateBranch(
            @TargetAggregateIdentifier UUID institutionId,
            UUID branchId
    ) {}

    public record SuspendBranch(
            @TargetAggregateIdentifier UUID institutionId,
            UUID branchId,
            String reason
    ) {}

    public record CloseBranch(
            @TargetAggregateIdentifier UUID institutionId,
            UUID branchId,
            String closureReason
    ) {}

    public record ReopenBranch(
            @TargetAggregateIdentifier UUID institutionId,
            UUID branchId
    ) {}
}