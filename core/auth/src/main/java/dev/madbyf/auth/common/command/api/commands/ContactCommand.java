package dev.madbyf.auth.common.command.api.commands;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class ContactCommand {

    public record Add(
        @TargetAggregateIdentifier String contactValue,
        String contactType,
        String ownerEntity,
        UUID ownerId
    ) {
    }

    public record Verify(
        @TargetAggregateIdentifier String contactValue,
        UUID ownerId
    ) {
    }

    public record Remove(
        @TargetAggregateIdentifier String contactValue,
        UUID ownerId
    ) {
    }

    public record LinkToProfile(
        @TargetAggregateIdentifier String contactValue,
        UUID entityProfileId
    ) {
    }
    
}
