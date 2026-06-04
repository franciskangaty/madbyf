package dev.madbyf.authorization.authorization.api.dto;

import java.time.Instant;
import java.util.Set;

public record ClientResponse(
        String id,
        String clientId,
        Instant clientIdIssuedAt,
        Instant clientSecretExpiresAt,
        String clientName,
        Set<ClientAuthenticationMethodType> clientAuthenticationMethods,
        Set<AuthorizationGrantTypeValue> authorizationGrantTypes,
        Set<String> redirectUris,
        Set<String> postLogoutRedirectUris,
        Set<String> scopes,
        boolean requireProofKey,
        boolean requireAuthorizationConsent,
        boolean reuseRefreshTokens,
        Long authorizationCodeTimeToLiveSeconds,
        Long accessTokenTimeToLiveSeconds,
        Long refreshTokenTimeToLiveSeconds,
        Long deviceCodeTimeToLiveSeconds
) {
}
