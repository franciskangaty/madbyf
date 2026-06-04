package dev.madbyf.authorization.authorization.api.dto;

import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.util.Set;

public record ClientUpdateRequest(
        String clientId,
        String clientSecret,
        Instant clientSecretExpiresAt,
        String clientName,
        Set<ClientAuthenticationMethodType> clientAuthenticationMethods,
        Set<AuthorizationGrantTypeValue> authorizationGrantTypes,
        Set<String> redirectUris,
        Set<String> postLogoutRedirectUris,
        Set<String> scopes,
        Boolean requireProofKey,
        Boolean requireAuthorizationConsent,
        Boolean reuseRefreshTokens,
        @Positive
        Long authorizationCodeTimeToLiveSeconds,
        @Positive
        Long accessTokenTimeToLiveSeconds,
        @Positive
        Long refreshTokenTimeToLiveSeconds,
        @Positive
        Long deviceCodeTimeToLiveSeconds
) {
}
