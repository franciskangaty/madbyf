package dev.madbyf.main.institution.api;

import java.util.concurrent.CompletableFuture;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.madbyf.main.institution.dto.InstitutionQueryDtos.InstitutionSearchRequest;
import dev.madbyf.main.institution.query.InstitutionQueries.FindInstitution;
import dev.madbyf.main.institution.query.InstitutionQueries.FindInstitutions;
import dev.madbyf.main.institution.query.InstitutionQueries.InstitutionFilter;
import dev.madbyf.main.institution.query.InstitutionQueries.LookupType;
import dev.madbyf.main.institution.query.InstitutionQueries.SortDirection;
import dev.madbyf.main.institution.view.InstitutionView;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/institutions")
@RequiredArgsConstructor
public class InstitutionQueryController {
   
    private final QueryGateway queryGateway;

    @GetMapping
    public CompletableFuture<InstitutionView> search(
            InstitutionSearchRequest request
    ){
        return queryGateway.query(
            new FindInstitutions(
                new InstitutionFilter(
                    request.registrationNumber(),
                    request.legalName(),
                    request.displayName(),
                    request.country(),
                    request.city(),
                    request.ownershipType(),
                    request.status(),
                    request.registrationVerified()
                ),
                request.page()==null?0:request.page(),
                request.size()==null?20:request.size(),
                request.sortBy(),
                SortDirection.valueOf(
                    request.direction()==null
                    ?"ASC"
                    :request.direction()
                )
            ),
            InstitutionView.class
        );
    }

    @GetMapping("/{type}/{value}")
    public CompletableFuture<InstitutionView> findOne(
            @PathVariable LookupType type,
            @PathVariable String value
    ){
        return queryGateway.query(
            new FindInstitution(
                type,
                value
            ),
            InstitutionView.class

        );

    }

}