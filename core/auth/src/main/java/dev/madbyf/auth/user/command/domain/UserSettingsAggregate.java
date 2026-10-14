package dev.madbyf.auth.user.command.domain;

import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate 
public class UserSettingsAggregate {
    @AggregateIdentifier 
    private String userId;
    
}
