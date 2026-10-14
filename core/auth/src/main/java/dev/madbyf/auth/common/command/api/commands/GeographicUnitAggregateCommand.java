package dev.madbyf.auth.common.command.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Commands for the GeographicUnit aggregate (common module).
 * Reference data forming a tree: COUNTRY > REGION > COUNTY > CITY > STREET > BUILDING.
 * One aggregate instance per unit. Purely geographic: it knows nothing about
 * who governs it. Jurisdiction is tracked separately in the governance module,
 * which references these units by id.
 * Addresses are built by any entity by pointing at a unit in this tree.
 */
public final class GeographicUnitAggregateCommand {

    private GeographicUnitAggregateCommand() {}

    // ---- CRUD ----

    /** parentUnitId is null only for COUNTRY. New units below city level may be proposed by entities (status PENDING) and confirmed later. */
    public record Create(
            @TargetAggregateIdentifier String unitId,
            String name,
            UnitLevel level,
            String parentUnitId,
            String code // nullable: e.g. ISO 3166 code, postal area code
    ) {}

    public record Rename(
            @TargetAggregateIdentifier String unitId,
            String name
    ) {}

    /** Re-parents a unit, e.g. a boundary change moving a city into a different region. */
    public record Move(
            @TargetAggregateIdentifier String unitId,
            String newParentUnitId,
            String reason
    ) {}

    // ---- Status modifiers ----

    /** Confirms an entity-proposed unit (street/building), making it selectable by everyone. */
    public record Confirm(
            @TargetAggregateIdentifier String unitId,
            String confirmedByUserId
    ) {}

    public record Reject(
            @TargetAggregateIdentifier String unitId,
            String reason
    ) {}

    public record Deprecate(
            @TargetAggregateIdentifier String unitId,
            String reason,
            String replacedByUnitId // nullable: e.g. merged municipalities
    ) {}

    public enum UnitLevel { COUNTRY, REGION, COUNTY, CITY, STREET, BUILDING }
}