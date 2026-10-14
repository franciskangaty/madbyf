package dev.madbyf.main.institution.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import dev.madbyf.main.institution.repository.impl.InstitutionViewRepositoryCustom;
import dev.madbyf.main.institution.view.InstitutionView;
import reactor.core.publisher.Mono;

public interface InstitutionViewRepository
        extends ReactiveCrudRepository<InstitutionView, UUID>,
       InstitutionViewRepositoryCustom {

    Mono<InstitutionView> findByRegistrationNumber(String registrationNumber);

    // Flux<InstitutionView> findByCountry(String country);

    // Flux<InstitutionView> findByRegion(String region);

    // Flux<InstitutionView> findByCity(String city);

    // Flux<InstitutionView> findByOwnershipType(String ownershipType);

    // Flux<InstitutionView> findByStatus(InstitutionStatus status);

    // Flux<InstitutionView> findByRegistrationVerified(boolean registrationVerified);

    // Flux<InstitutionView> findByLegalNameContainingIgnoreCase(String legalName);

    // Flux<InstitutionView> findByDisplayNameContainingIgnoreCase(String displayName);

    // Mono<Boolean> existsByRegistrationNumber(String registrationNumber);

    // @Query("""
    //         SELECT *
    //         FROM institutions
    //         WHERE status <> 'DELETED'
    //         ORDER BY display_name
    //         """)
    // Flux<InstitutionView> findAllActiveRecords();
    
}