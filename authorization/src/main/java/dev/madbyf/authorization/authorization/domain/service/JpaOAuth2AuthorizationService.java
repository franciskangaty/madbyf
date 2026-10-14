package dev.madbyf.authorization.authorization.domain.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.madbyf.authorization.authorization.domain.model.Authorization;
import dev.madbyf.authorization.authorization.domain.repository.AuthorizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService {
    private final AuthorizationRepository authorizationRepository;
    private final RegisteredClientRepository registeredClientRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        authorizationRepository.save(toEntity(authorization));
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        authorizationRepository.deleteById(authorization.getId());
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return authorizationRepository.findById(id).map(this::toObject).orElse(null);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");

        Optional<Authorization> result;
        if (tokenType == null) {
            result = authorizationRepository.findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(
                    token,
                    token,
                    token,
                    token,
                    token,
                    token,
                    token
            );
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            result = authorizationRepository.findByState(token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            result = authorizationRepository.findByAuthorizationCodeValue(token);
        } else if (OAuth2ParameterNames.ACCESS_TOKEN.equals(tokenType.getValue())) {
            result = authorizationRepository.findByAccessTokenValue(token);
        } else if (OAuth2ParameterNames.REFRESH_TOKEN.equals(tokenType.getValue())) {
            result = authorizationRepository.findByRefreshTokenValue(token);
        } else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            result = authorizationRepository.findByOidcIdTokenValue(token);
        } else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
            result = authorizationRepository.findByUserCodeValue(token);
        } else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
            result = authorizationRepository.findByDeviceCodeValue(token);
        } else {
            result = Optional.empty();
        }

        return result.map(this::toObject).orElse(null);
    }

    private OAuth2Authorization toObject(Authorization entity) {
        var registeredClient = registeredClientRepository.findById(entity.getRegisteredClientId());
        if (registeredClient == null) {
            throw new DataRetrievalFailureException(
                    "The RegisteredClient with id '" + entity.getRegisteredClientId() + "' was not found.");
        }

        var builder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .id(entity.getId())
                .principalName(entity.getPrincipalName())
                .authorizationGrantType(resolveAuthorizationGrantType(entity.getAuthorizationGrantType()))
                .authorizedScopes(StringUtils.commaDelimitedListToSet(entity.getAuthorizedScopes()))
                .attributes(attributes -> attributes.putAll(parseMap(entity.getAttributes())));

        if (entity.getState() != null) {
            builder.attribute(OAuth2ParameterNames.STATE, entity.getState());
        }
        if (entity.getAuthorizationCodeValue() != null) {
            var authorizationCode = new OAuth2AuthorizationCode(
                    entity.getAuthorizationCodeValue(),
                    entity.getAuthorizationCodeIssuedAt(),
                    entity.getAuthorizationCodeExpiresAt());
            builder.token(authorizationCode, metadata -> metadata.putAll(parseMap(entity.getAuthorizationCodeMetadata())));
        }
        if (entity.getAccessTokenValue() != null) {
            var accessToken = new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    entity.getAccessTokenValue(),
                    entity.getAccessTokenIssuedAt(),
                    entity.getAccessTokenExpiresAt(),
                    StringUtils.commaDelimitedListToSet(entity.getAccessTokenScopes()));
            builder.token(accessToken, metadata -> metadata.putAll(parseMap(entity.getAccessTokenMetadata())));
        }
        if (entity.getRefreshTokenValue() != null) {
            var refreshToken = new OAuth2RefreshToken(
                    entity.getRefreshTokenValue(),
                    entity.getRefreshTokenIssuedAt(),
                    entity.getRefreshTokenExpiresAt());
            builder.token(refreshToken, metadata -> metadata.putAll(parseMap(entity.getRefreshTokenMetadata())));
        }
        if (entity.getOidcIdTokenValue() != null) {
            var idToken = new OidcIdToken(
                    entity.getOidcIdTokenValue(),
                    entity.getOidcIdTokenIssuedAt(),
                    entity.getOidcIdTokenExpiresAt(),
                    parseMap(entity.getOidcIdTokenClaims()));
            builder.token(idToken, metadata -> metadata.putAll(parseMap(entity.getOidcIdTokenMetadata())));
        }
        if (entity.getUserCodeValue() != null) {
            var userCode = new OAuth2UserCode(
                    entity.getUserCodeValue(),
                    entity.getUserCodeIssuedAt(),
                    entity.getUserCodeExpiresAt());
            builder.token(userCode, metadata -> metadata.putAll(parseMap(entity.getUserCodeMetadata())));
        }
        if (entity.getDeviceCodeValue() != null) {
            var deviceCode = new OAuth2DeviceCode(
                    entity.getDeviceCodeValue(),
                    entity.getDeviceCodeIssuedAt(),
                    entity.getDeviceCodeExpiresAt());
            builder.token(deviceCode, metadata -> metadata.putAll(parseMap(entity.getDeviceCodeMetadata())));
        }

        return builder.build();
    }

    private Authorization toEntity(OAuth2Authorization authorization) {
        var entity = new Authorization();
        entity.setId(authorization.getId());
        entity.setRegisteredClientId(authorization.getRegisteredClientId());
        entity.setPrincipalName(authorization.getPrincipalName());
        entity.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
        entity.setAuthorizedScopes(StringUtils.collectionToCommaDelimitedString(authorization.getAuthorizedScopes()));
        entity.setAttributes(writeMap(authorization.getAttributes()));
        entity.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));

        var authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        setTokenValues(
                authorizationCode,
                entity::setAuthorizationCodeValue,
                entity::setAuthorizationCodeIssuedAt,
                entity::setAuthorizationCodeExpiresAt,
                entity::setAuthorizationCodeMetadata
        );

        var accessToken = authorization.getToken(OAuth2AccessToken.class);
        setTokenValues(
                accessToken,
                entity::setAccessTokenValue,
                entity::setAccessTokenIssuedAt,
                entity::setAccessTokenExpiresAt,
                entity::setAccessTokenMetadata
        );
        if (accessToken != null) {
            entity.setAccessTokenType(accessToken.getToken().getTokenType().getValue());
            entity.setAccessTokenScopes(StringUtils.collectionToCommaDelimitedString(accessToken.getToken().getScopes()));
        }

        var refreshToken = authorization.getToken(OAuth2RefreshToken.class);
        setTokenValues(
                refreshToken,
                entity::setRefreshTokenValue,
                entity::setRefreshTokenIssuedAt,
                entity::setRefreshTokenExpiresAt,
                entity::setRefreshTokenMetadata
        );

        var oidcIdToken = authorization.getToken(OidcIdToken.class);
        setTokenValues(
                oidcIdToken,
                entity::setOidcIdTokenValue,
                entity::setOidcIdTokenIssuedAt,
                entity::setOidcIdTokenExpiresAt,
                entity::setOidcIdTokenMetadata
        );
        if (oidcIdToken != null) {
            entity.setOidcIdTokenClaims(writeMap(oidcIdToken.getClaims()));
        }

        var userCode = authorization.getToken(OAuth2UserCode.class);
        setTokenValues(
                userCode,
                entity::setUserCodeValue,
                entity::setUserCodeIssuedAt,
                entity::setUserCodeExpiresAt,
                entity::setUserCodeMetadata
        );

        var deviceCode = authorization.getToken(OAuth2DeviceCode.class);
        setTokenValues(
                deviceCode,
                entity::setDeviceCodeValue,
                entity::setDeviceCodeIssuedAt,
                entity::setDeviceCodeExpiresAt,
                entity::setDeviceCodeMetadata
        );

        return entity;
    }

    private void setTokenValues(
            OAuth2Authorization.Token<?> token,
            Consumer<String> tokenValueConsumer,
            Consumer<Instant> issuedAtConsumer,
            Consumer<Instant> expiresAtConsumer,
            Consumer<String> metadataConsumer
    ) {
        if (token == null) {
            return;
        }

        OAuth2Token oAuth2Token = token.getToken();
        tokenValueConsumer.accept(oAuth2Token.getTokenValue());
        issuedAtConsumer.accept(oAuth2Token.getIssuedAt());
        expiresAtConsumer.accept(oAuth2Token.getExpiresAt());
        metadataConsumer.accept(writeMap(token.getMetadata()));
    }

    private Map<String, Object> parseMap(String data) {
        if (!StringUtils.hasText(data)) {
            return new HashMap<>();
        }

        try {
            return objectMapper.readValue(data, new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private String writeMap(Map<String, Object> metadata) {
        try {
            return objectMapper.writeValueAsString(metadata == null ? Map.of() : metadata);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        }
        if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        }
        if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        if (AuthorizationGrantType.JWT_BEARER.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.JWT_BEARER;
        }
        if (AuthorizationGrantType.DEVICE_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.DEVICE_CODE;
        }
        if (AuthorizationGrantType.TOKEN_EXCHANGE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.TOKEN_EXCHANGE;
        }
        return new AuthorizationGrantType(authorizationGrantType);
    }
}
