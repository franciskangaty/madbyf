package dev.madbyf.auth.user.query.projection;

import java.util.UUID;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.madbyf.auth.user.command.domain.events.UserAccountAggregateEvent;
import dev.madbyf.auth.user.query.model.UserAccountView;
import dev.madbyf.auth.user.query.repository.UserAccountViewRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class UserAccountViewProjection {

    private final UserAccountViewRepository repository;

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.Created event) {

        repository.save(new UserAccountView(
                event.userId(),
                event.firstName(),
                event.middleName(),
                event.lastName(),
                event.dateOfBirth()
        ));
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.NamesUpdated event) {

        UserAccountView view = get(event.userId());

        view.updateNames(
                event.newFirstName(),
                event.newMiddleName(),
                event.newLastName()
        );
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.DateOfBirthUpdated event) {

        get(event.userId())
                .updateDateOfBirth(event.newDateOfBirth());
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.ProfilePhotoUrlSet event) {

        get(event.userId())
                .setProfilePhotoUrl(event.newProfilePhotoUrl());
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.NameVerified event) {

        get(event.userId())
                .markNameVerified();
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.AgeVerified event) {

        get(event.userId())
                .markAgeVerified();
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.PasswordSet event) {

        get(event.userId())
                .markPasswordSet();
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.PasswordRemoved event) {

        get(event.userId())
                .markPasswordRemoved();
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.Enabled event) {

        get(event.userId())
                .enable();
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.Disabled event) {

        get(event.userId())
                .disable();
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.Locked event) {

        get(event.userId())
                .lock();
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.Unlocked event) {

        get(event.userId())
                .unlock();
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.CredentialsExpired event) {

        get(event.userId())
                .expireCredentials();
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.CredentialsRenewed event) {

        get(event.userId())
                .renewCredentials();
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.AccountExpired event) {

        get(event.userId())
                .expireAccount();
    }

    @EventHandler
    @Transactional
    public void on(UserAccountAggregateEvent.AccountReactivated event) {

        get(event.userId())
                .reactivateAccount();
    }

    private UserAccountView get(UUID userId) {
        return repository.findById(userId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "UserAccountView not found: " + userId
                        ));
    }
}
