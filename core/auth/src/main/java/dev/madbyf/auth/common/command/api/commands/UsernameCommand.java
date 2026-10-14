package dev.madbyf.auth.common.command.api.commands;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class UsernameCommand {

    public record Reserve(
        @TargetAggregateIdentifier String username,
        String entity,
        UUID entityId
    ) {
    }

    public record Release(@TargetAggregateIdentifier String username) {
    }
}