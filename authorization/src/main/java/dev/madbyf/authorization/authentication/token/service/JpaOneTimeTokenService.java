package dev.madbyf.authorization.authentication.token.service;

import java.time.Instant;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.madbyf.authorization.authentication.token.model.JpaOneTimeToken;
import dev.madbyf.authorization.authentication.token.repository.JpaOneTimeTokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class JpaOneTimeTokenService implements OneTimeTokenService {
    private final JpaOneTimeTokenRepository oneTimeTokenRepository;

    private static final long TOKEN_EXPIRATION_TIME = 5 * 60; // 5 minutes in seconds

    @Override
    public OneTimeToken generate(GenerateOneTimeTokenRequest request) {
        // Generate a random token
        String token = generateRandomToken();

        // Create a new OneTimeToken entity
        JpaOneTimeToken oneTimeToken = JpaOneTimeToken.builder()
                .token(token)
                .used(false)
                .expiresAt(Instant.now().plusSeconds(TOKEN_EXPIRATION_TIME))
                .build();

        // Save the token to the database
        oneTimeTokenRepository.save(oneTimeToken);

        return oneTimeToken;
    }

    @Override
    public @Nullable OneTimeToken consume(OneTimeTokenAuthenticationToken authenticationToken) {
        return oneTimeTokenRepository.findByTokenAndUsedFalseAndExpiresAtAfter(authenticationToken.getTokenValue(), Instant.now())
                .map(token -> {
                    token.setUsed(true);
                    oneTimeTokenRepository.save(token);
                    return token;
                })
                .orElse(null);
    }

    private String generateRandomToken() {
        // Generate a random token using UUID
        return java.util.UUID.randomUUID().toString();
    }

}
