package dev.madbyf.auth.user.query.api;

import java.util.Map;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.madbyf.auth.user.query.api.dtos.UserAccountResponse;
import dev.madbyf.auth.user.query.api.queries.UserAccountQuery;

@RestController
@RequestMapping("/api/{version}/users")
public class UserAccountQueryController {

    private final QueryGateway queryGateway;

    public UserAccountQueryController(
            QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public Page<UserAccountResponse> findAll(
            @RequestParam Map<String, String> params) {

        int page = Integer.parseInt(
                params.getOrDefault("page", "0")
        );

        int size = Integer.parseInt(
                params.getOrDefault("size", "20")
        );

        Sort sort = Sort.unsorted();

        // Remove pagination/sort parameters from filters.
        Map<String, Object> filters =
                new java.util.HashMap<>(params);

        filters.remove("page");
        filters.remove("size");
        filters.remove("sort");

        return queryGateway.query(
                new UserAccountQuery.FindAll(
                        filters,
                        page,
                        size,
                        sort
                ),
                ParameterizedTypeReference
                        .forType(
                                Page.class
                        )
        ).join();
    }
}
