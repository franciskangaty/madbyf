package dev.madbyf.auth.user.query.repository.specification;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import dev.madbyf.auth.user.query.model.UserAccountView;

public final class UserAccountViewSpecification {

    private UserAccountViewSpecification() {
    }

    public static Specification<UserAccountView> from(
            Map<String, Object> filters) {

        Specification<UserAccountView> specification =
                (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        for (Map.Entry<String, Object> entry : filters.entrySet()) {

            String attribute = entry.getKey();
            Object value = entry.getValue();

            specification = specification.and(
                    equals(attribute, value)
            );
        }

        return specification;
    }

    private static Specification<UserAccountView> equals(
            String attribute,
            Object value) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get(attribute),
                        value
                );
    }
}
