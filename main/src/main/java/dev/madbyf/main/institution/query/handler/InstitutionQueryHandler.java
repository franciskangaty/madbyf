package dev.madbyf.main.institution.query.handler;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.axonframework.queryhandling.QueryHandler;

import org.springframework.stereotype.Component;

import dev.madbyf.main.institution.query.InstitutionQueries.*;
import dev.madbyf.main.institution.repository.InstitutionViewRepository;
import dev.madbyf.main.institution.view.InstitutionView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class InstitutionQueryHandler {

    private final InstitutionViewRepository repository;

    @QueryHandler
    public Flux<InstitutionView> handle(
            FindInstitutions query
    ){
        return repository.search(
                query.filter(),
                query.page(),
                query.size(),
                query.sortBy(),
                query.direction().name()
        );
    }

    @QueryHandler
    public Mono<InstitutionView> handle(
            FindInstitution query
    ){

        return switch(query.type()){
            case ID ->
                repository.findById(
                    UUID.fromString(query.value())
                );
            case REGISTRATION_NUMBER ->
                repository.findByRegistrationNumber(
                    query.value()
                );
            default ->
                Mono.empty();

        };

    }

}