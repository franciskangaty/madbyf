package dev.madbyf.auth.governance.command.api.commands;

import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * One instance per jurisdiction. A jurisdiction has a scope type:
 *  - GEOGRAPHIC: covers GeographicUnits (e.g. a national ministry's territory).
 *  - ORGANIZATIONAL: covers a group and its descendants (e.g. a company's own
 *    governance unit over itself and its subsidiaries).
 * Jurisdictions chain via parentJurisdictionCode, so a subsidiary can have its
 * own jurisdiction beneath its parent company's. Resolution for a registrant:
 * within each required scope type, the most specific applicable jurisdiction
 * (matching the group type's governance domain) supplies the governing group.
 */
public final class GovernanceJurisdictionAggregateCommand {

    private GovernanceJurisdictionAggregateCommand() {}

    // ---- CRUD ----

    public record RegisterJurisdiction(
            @TargetAggregateIdentifier String jurisdictionCode,
            String name,
            ScopeType scopeType,
            String parentJurisdictionCode, // nullable: enclosing jurisdiction of the same scope type
            String rootGroupId             // ORGANIZATIONAL only: the group whose subtree is covered
    ) {}

    public record RenameJurisdiction(
            @TargetAggregateIdentifier String jurisdictionCode,
            String name
    ) {}

    // ---- Status modifiers ----

    public record Deactivate(
            @TargetAggregateIdentifier String jurisdictionCode,
            String reason
    ) {}

    public record Reactivate(
            @TargetAggregateIdentifier String jurisdictionCode
    ) {}

    // ---- Business commands ----

    /** GEOGRAPHIC only: which geographic units this jurisdiction covers. ORGANIZATIONAL coverage is derived from the group tree under rootGroupId. */
    public record AssignCoverage(
            @TargetAggregateIdentifier String jurisdictionCode,
            List<String> geographicUnitIds
    ) {}

    public record RemoveCoverage(
            @TargetAggregateIdentifier String jurisdictionCode,
            List<String> geographicUnitIds
    ) {}

    /** Only fired as the effect of an activated GovernanceDesignation. */
    public record AssignGoverningGroup(
            @TargetAggregateIdentifier String jurisdictionCode,
            String groupId,
            String domain, // nullable: null = all domains
            String designationId
    ) {}

    public record RevokeGoverningGroup(
            @TargetAggregateIdentifier String jurisdictionCode,
            String groupId,
            String domain,
            String reason
    ) {}

    public enum ScopeType { GEOGRAPHIC, ORGANIZATIONAL }
}