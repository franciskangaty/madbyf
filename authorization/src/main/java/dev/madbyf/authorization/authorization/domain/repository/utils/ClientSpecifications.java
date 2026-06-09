package dev.madbyf.authorization.authorization.domain.repository.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import dev.madbyf.authorization.authorization.api.dto.ClientSearchCriteria;
import dev.madbyf.authorization.authorization.domain.model.Client;
import jakarta.persistence.criteria.Predicate;

@Component
public final class ClientSpecifications {

    private ClientSpecifications() {
    }

    public static Specification<Client> search(ClientSearchCriteria criteria) {
        return (root, query, cb) -> {
    
            List<Predicate> predicates = new ArrayList<>();
    
            if (criteria.clientId() != null && !criteria.clientId().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("clientId")),
                                "%" + criteria.clientId().toLowerCase() + "%"
                        )
                );
            }
    
            if (criteria.clientName() != null && !criteria.clientName().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("clientName")),
                                "%" + criteria.clientName().toLowerCase() + "%"
                        )
                );
            }
    
            if (criteria.clientAuthenticationMethod() != null) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("clientAuthenticationMethods")),
                                "%" + criteria.clientAuthenticationMethod()
                                        .name()
                                        .toLowerCase() + "%"
                        )
                );
            }
    
            if (criteria.authorizationGrantType() != null) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("authorizationGrantTypes")),
                                "%" + criteria.authorizationGrantType()
                                        .name()
                                        .toLowerCase() + "%"
                        )
                );
            }
    
            if (criteria.scope() != null && !criteria.scope().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("scopes")),
                                "%" + criteria.scope().toLowerCase() + "%"
                        )
                );
            }
    
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}