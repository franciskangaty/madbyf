package dev.madbyf.main.institution.repository.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import org.springframework.data.relational.core.query.*;

import org.springframework.stereotype.Repository;

import dev.madbyf.main.institution.query.InstitutionQueries.InstitutionFilter;
import dev.madbyf.main.institution.view.InstitutionView;
import reactor.core.publisher.Flux;



@Repository
@RequiredArgsConstructor
public class InstitutionViewRepositoryImpl
        implements InstitutionViewRepositoryCustom {


    private final R2dbcEntityTemplate template;



    @Override
    public Flux<InstitutionView> search(
            InstitutionFilter filter,
            int page,
            int size,
            String sortBy,
            String direction
    ){


        Criteria criteria = Criteria.empty();



        if(filter.registrationNumber()!=null)

            criteria = criteria.and(
                Criteria.where("registration_number")
                .is(filter.registrationNumber())
            );



        if(filter.legalName()!=null)

            criteria = criteria.and(
                Criteria.where("legal_name")
                .like("%"+filter.legalName()+"%")
            );



        if(filter.country()!=null)

            criteria = criteria.and(
                Criteria.where("country")
                .is(filter.country())
            );



        Query query =
            Query.query(criteria)
            .limit(size)
            .offset((long)page * size);



        if(sortBy!=null){

            query = query.sort(
                Sort.by(
                    "DESC".equals(direction)
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC,
                    sortBy
                )
            );
        }



        return template.select(
                query,
                InstitutionView.class
        );

    }

}