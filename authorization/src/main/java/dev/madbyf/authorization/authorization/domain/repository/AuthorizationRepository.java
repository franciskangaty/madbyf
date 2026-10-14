package dev.madbyf.authorization.authorization.domain.repository;

import dev.madbyf.authorization.authorization.domain.model.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizationRepository extends JpaRepository<Authorization, String> {
    Optional<Authorization> findByState(String state);

    Optional<Authorization> findByAuthorizationCodeValue(String authorizationCode);

    Optional<Authorization> findByAccessTokenValue(String accessToken);

    Optional<Authorization> findByRefreshTokenValue(String refreshToken);

    Optional<Authorization> findByOidcIdTokenValue(String idToken);

    Optional<Authorization> findByUserCodeValue(String userCode);

    Optional<Authorization> findByDeviceCodeValue(String deviceCode);

    Optional<Authorization> findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(
            String state,
            String authorizationCode,
            String accessToken,
            String refreshToken,
            String idToken,
            String userCode,
            String deviceCode
    );
}
