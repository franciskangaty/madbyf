package dev.madbyf.main.institution.repository.impl;

import dev.madbyf.main.institution.query.InstitutionQueries.InstitutionFilter;
import dev.madbyf.main.institution.view.InstitutionView;
import reactor.core.publisher.Flux;

public interface InstitutionViewRepositoryCustom {

    Flux<InstitutionView> search(
            InstitutionFilter filter,
            int page,
            int size,
            String sortBy,
            String direction
    );

}