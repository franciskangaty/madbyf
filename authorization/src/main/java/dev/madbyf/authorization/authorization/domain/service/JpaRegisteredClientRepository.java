package dev.madbyf.authorization.authorization.domain.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.madbyf.authorization.authorization.domain.model.Client;
import dev.madbyf.authorization.authorization.domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JpaRegisteredClientRepository implements RegisteredClientRepository {
    private final ClientRepository clientRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        clientRepository.save(toEntity(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return clientRepository.findById(id).map(this::toObject).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        return clientRepository.findByClientId(clientId).map(this::toObject).orElse(null);
    }

    private RegisteredClient toObject(Client client) {
        var clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(client.getClientAuthenticationMethods());
        var authorizationGrantTypes = StringUtils.commaDelimitedListToSet(client.getAuthorizationGrantTypes());
        var redirectUris = StringUtils.commaDelimitedListToSet(client.getRedirectUris());
        var postLogoutRedirectUris = StringUtils.commaDelimitedListToSet(client.getPostLogoutRedirectUris());
        var scopes = StringUtils.commaDelimitedListToSet(client.getScopes());

        var builder = RegisteredClient.withId(client.getId())
                .clientId(client.getClientId())
                .clientIdIssuedAt(client.getClientIdIssuedAt())
                .clientSecret(client.getClientSecret())
                .clientSecretExpiresAt(client.getClientSecretExpiresAt())
                .clientName(client.getClientName())
                .clientSettings(ClientSettings.withSettings(normalizeClientSettings(parseMap(client.getClientSettings()))).build())
                .tokenSettings(TokenSettings.withSettings(normalizeTokenSettings(parseMap(client.getTokenSettings()))).build());

        clientAuthenticationMethods.forEach(method ->
                builder.clientAuthenticationMethod(resolveClientAuthenticationMethod(method)));
        authorizationGrantTypes.forEach(grantType ->
                builder.authorizationGrantType(resolveAuthorizationGrantType(grantType)));
        redirectUris.forEach(builder::redirectUri);
        postLogoutRedirectUris.forEach(builder::postLogoutRedirectUri);
        scopes.forEach(builder::scope);

        return builder.build();
    }

    private Client toEntity(RegisteredClient registeredClient) {
        var entity = new Client();
        entity.setId(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        entity.setClientName(registeredClient.getClientName());
        entity.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(
                registeredClient.getClientAuthenticationMethods().stream()
                        .map(ClientAuthenticationMethod::getValue)
                        .toList()
        ));
        entity.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(
                registeredClient.getAuthorizationGrantTypes().stream()
                        .map(AuthorizationGrantType::getValue)
                        .toList()
        ));
        entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
        entity.setPostLogoutRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getPostLogoutRedirectUris()));
        entity.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
        entity.setClientSettings(writeMap(simplifyClientSettings(registeredClient.getClientSettings().getSettings())));
        entity.setTokenSettings(writeMap(simplifyTokenSettings(registeredClient.getTokenSettings().getSettings())));
        return entity;
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

    private String writeMap(Map<String, Object> data) {
        try {
            return objectMapper.writeValueAsString(data == null ? Map.of() : data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private Map<String, Object> simplifyClientSettings(Map<String, Object> settings) {
        var result = new HashMap<>(settings);
        var signingAlgorithm = result.get(ConfigurationSettingNames.Client.TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM);
        if (signingAlgorithm instanceof JwsAlgorithm algorithm) {
            result.put(ConfigurationSettingNames.Client.TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM, algorithm.getName());
        }
        return result;
    }

    private Map<String, Object> normalizeClientSettings(Map<String, Object> settings) {
        var result = new HashMap<>(settings);
        var signingAlgorithm = result.get(ConfigurationSettingNames.Client.TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM);
        if (signingAlgorithm instanceof String algorithm) {
            result.put(ConfigurationSettingNames.Client.TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM, resolveJwsAlgorithm(algorithm));
        }
        return result;
    }

    private Map<String, Object> simplifyTokenSettings(Map<String, Object> settings) {
        var result = new HashMap<>(settings);
        simplifyDuration(result, ConfigurationSettingNames.Token.AUTHORIZATION_CODE_TIME_TO_LIVE);
        simplifyDuration(result, ConfigurationSettingNames.Token.ACCESS_TOKEN_TIME_TO_LIVE);
        simplifyDuration(result, ConfigurationSettingNames.Token.DEVICE_CODE_TIME_TO_LIVE);
        simplifyDuration(result, ConfigurationSettingNames.Token.REFRESH_TOKEN_TIME_TO_LIVE);

        var accessTokenFormat = result.get(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT);
        if (accessTokenFormat instanceof OAuth2TokenFormat tokenFormat) {
            result.put(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT, tokenFormat.getValue());
        }
        var signatureAlgorithm = result.get(ConfigurationSettingNames.Token.ID_TOKEN_SIGNATURE_ALGORITHM);
        if (signatureAlgorithm instanceof SignatureAlgorithm algorithm) {
            result.put(ConfigurationSettingNames.Token.ID_TOKEN_SIGNATURE_ALGORITHM, algorithm.getName());
        }
        return result;
    }

    private Map<String, Object> normalizeTokenSettings(Map<String, Object> settings) {
        var result = new HashMap<>(settings);
        normalizeDuration(result, ConfigurationSettingNames.Token.AUTHORIZATION_CODE_TIME_TO_LIVE);
        normalizeDuration(result, ConfigurationSettingNames.Token.ACCESS_TOKEN_TIME_TO_LIVE);
        normalizeDuration(result, ConfigurationSettingNames.Token.DEVICE_CODE_TIME_TO_LIVE);
        normalizeDuration(result, ConfigurationSettingNames.Token.REFRESH_TOKEN_TIME_TO_LIVE);

        var accessTokenFormat = result.get(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT);
        if (accessTokenFormat instanceof String tokenFormat) {
            result.put(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT, resolveOAuth2TokenFormat(tokenFormat));
        }
        var signatureAlgorithm = result.get(ConfigurationSettingNames.Token.ID_TOKEN_SIGNATURE_ALGORITHM);
        if (signatureAlgorithm instanceof String algorithm) {
            result.put(ConfigurationSettingNames.Token.ID_TOKEN_SIGNATURE_ALGORITHM, SignatureAlgorithm.from(algorithm));
        }
        return result;
    }

    private void simplifyDuration(Map<String, Object> settings, String key) {
        var value = settings.get(key);
        if (value instanceof Duration duration) {
            settings.put(key, duration.toSeconds());
        }
    }

    private void normalizeDuration(Map<String, Object> settings, String key) {
        var value = settings.get(key);
        if (value instanceof Number seconds) {
            settings.put(key, Duration.ofSeconds(seconds.longValue()));
        } else if (value instanceof String text && StringUtils.hasText(text)) {
            settings.put(key, parseDuration(text));
        }
    }

    private Duration parseDuration(String text) {
        try {
            return Duration.ofSeconds(Long.parseLong(text));
        } catch (NumberFormatException ignored) {
            return Duration.parse(text);
        }
    }

    private OAuth2TokenFormat resolveOAuth2TokenFormat(String tokenFormat) {
        if (OAuth2TokenFormat.SELF_CONTAINED.getValue().equals(tokenFormat)) {
            return OAuth2TokenFormat.SELF_CONTAINED;
        }
        if (OAuth2TokenFormat.REFERENCE.getValue().equals(tokenFormat)) {
            return OAuth2TokenFormat.REFERENCE;
        }
        return new OAuth2TokenFormat(tokenFormat);
    }

    private JwsAlgorithm resolveJwsAlgorithm(String algorithm) {
        var signatureAlgorithm = SignatureAlgorithm.from(algorithm);
        if (signatureAlgorithm != null) {
            return signatureAlgorithm;
        }
        var macAlgorithm = MacAlgorithm.from(algorithm);
        if (macAlgorithm != null) {
            return macAlgorithm;
        }
        throw new IllegalArgumentException("Unsupported JWS algorithm: " + algorithm);
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

    private ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        }
        if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        }
        if (ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_JWT;
        }
        if (ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.PRIVATE_KEY_JWT;
        }
        if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        if (ClientAuthenticationMethod.TLS_CLIENT_AUTH.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.TLS_CLIENT_AUTH;
        }
        if (ClientAuthenticationMethod.SELF_SIGNED_TLS_CLIENT_AUTH.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.SELF_SIGNED_TLS_CLIENT_AUTH;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);
    }
}
