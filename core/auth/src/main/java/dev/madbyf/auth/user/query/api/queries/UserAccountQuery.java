package dev.madbyf.auth.user.query.api.queries;

import java.util.Map;

import org.springframework.data.domain.Sort;

public sealed interface UserAccountQuery
        permits UserAccountQuery.FindById,
                UserAccountQuery.FindAll {

    record FindById(
            java.util.UUID userId
    ) implements UserAccountQuery {
    }

    record FindAll(
            Map<String, Object> filters,
            int page,
            int size,
            Sort sort
    ) implements UserAccountQuery {

        public FindAll {
            filters = filters == null
                    ? Map.of()
                    : Map.copyOf(filters);

            if (page < 0) {
                throw new IllegalArgumentException(
                        "Page must be >= 0"
                );
            }

            if (size < 1) {
                throw new IllegalArgumentException(
                        "Size must be >= 1"
                );
            }

            if (sort == null) {
                sort = Sort.unsorted();
            }
        }
    }
}
