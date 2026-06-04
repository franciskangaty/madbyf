package dev.madbyf.authorization.authorization.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.util.Set;

public record ClientRegistrationRequest(
        @NotBlank
        String clientId,
        String clientSecret,
        Instant clientSecretExpiresAt,
        @NotBlank
        String clientName,
        @NotEmpty
        Set<ClientAuthenticationMethodType> clientAuthenticationMethods,
        @NotEmpty
        Set<AuthorizationGrantTypeValue> authorizationGrantTypes,
        Set<@NotBlank String> redirectUris,
        Set<@NotBlank String> postLogoutRedirectUris,
        @NotEmpty
        Set<@NotBlank String> scopes,
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
