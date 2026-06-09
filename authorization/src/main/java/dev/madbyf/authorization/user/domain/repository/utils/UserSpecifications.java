package dev.madbyf.authorization.user.domain.repository.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import dev.madbyf.authorization.user.api.dto.UserSearchCriteria;
import dev.madbyf.authorization.user.domain.model.Contact;
import dev.madbyf.authorization.user.domain.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Component
public final class UserSpecifications {

    private UserSpecifications() {
    }

    public static Specification<User> search(UserSearchCriteria criteria) {
        return (root, query, cb) -> {

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (hasText(criteria.username())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("username")),
                                "%" + criteria.username().toLowerCase() + "%"
                        )
                );
            }

            if (hasText(criteria.firstName())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("firstName")),
                                "%" + criteria.firstName().toLowerCase() + "%"
                        )
                );
            }

            if (hasText(criteria.lastName())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("lastName")),
                                "%" + criteria.lastName().toLowerCase() + "%"
                        )
                );
            }

            if (hasText(criteria.role())) {
                Join<User, String> roles = root.join("roles", JoinType.LEFT);

                predicates.add(
                        cb.equal(roles, criteria.role())
                );
            }

            if (criteria.enabled() != null) {
                predicates.add(
                        cb.equal(root.get("enabled"), criteria.enabled())
                );
            }

            if (criteria.verified() != null) {
                predicates.add(
                        cb.equal(root.get("verified"), criteria.verified())
                );
            }

            if (criteria.contactType() != null || hasText(criteria.contactValue())) {

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Contact> contact = subquery.from(Contact.class);

                List<Predicate> contactPredicates = new ArrayList<>();

                contactPredicates.add(
                        cb.equal(contact.get("user"), root)
                );

                if (criteria.contactType() != null) {
                    contactPredicates.add(
                            cb.equal(contact.get("type"), criteria.contactType())
                    );
                }

                if (hasText(criteria.contactValue())) {
                    contactPredicates.add(
                            cb.like(
                                    cb.lower(contact.get("value")),
                                    "%" + criteria.contactValue().toLowerCase() + "%"
                            )
                    );
                }

                subquery.select(contact.get("id"))
                        .where(contactPredicates.toArray(new Predicate[0]));

                predicates.add(cb.exists(subquery));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}