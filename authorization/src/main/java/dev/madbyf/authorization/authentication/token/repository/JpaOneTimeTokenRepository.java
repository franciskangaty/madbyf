package dev.madbyf.authorization.authentication.token.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.madbyf.authorization.authentication.token.model.JpaOneTimeToken;

public interface JpaOneTimeTokenRepository extends JpaRepository<JpaOneTimeToken, UUID> {
    Optional<JpaOneTimeToken> findByTokenAndUsedFalseAndExpiresAtAfter(String token, Instant expiresAt);

    void deleteByExpiresAtBefore(Instant dateTime);
}
