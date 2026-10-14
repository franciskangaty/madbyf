package dev.madbyf.auth.user.query.service;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.madbyf.auth.user.query.api.dtos.UserAccountResponse;
import dev.madbyf.auth.user.query.api.queries.UserAccountQuery;
import dev.madbyf.auth.user.query.model.UserAccountView;
import dev.madbyf.auth.user.query.repository.UserAccountViewRepository;
import dev.madbyf.auth.user.query.repository.specification.UserAccountViewSpecification;

@Component
public class UserAccountQueryHandler {

    private final UserAccountViewRepository repository;

    public UserAccountQueryHandler(
            UserAccountViewRepository repository) {
        this.repository = repository;
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public Page<UserAccountResponse> handle(
            UserAccountQuery.FindAll query) {

        PageRequest pageable = PageRequest.of(
                query.page(),
                query.size(),
                query.sort()
        );

        Page<UserAccountView> page =
                repository.findAll(
                        UserAccountViewSpecification.from(
                                query.filters()
                        ),
                        pageable
                );

        return page.map(this::toResponse);
    }

    private UserAccountResponse toResponse(
            UserAccountView view) {

        return new UserAccountResponse(
                view.getUserId(),
                view.getFirstName(),
                view.getMiddleName(),
                view.getLastName(),
                view.getDateOfBirth(),
                view.getProfilePhotoUrl(),
                view.isNameVerified(),
                view.isAgeVerified(),
                view.isPasswordSet(),
                view.isActive(),
                view.isSuspended(),
                view.isCredentialExpired(),
                view.isAccountExpired(),
                view.isLocked()
        );
    }
}
