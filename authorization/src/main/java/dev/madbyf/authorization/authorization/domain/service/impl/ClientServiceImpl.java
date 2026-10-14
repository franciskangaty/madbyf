package dev.madbyf.authorization.authorization.domain.service.impl;

import dev.madbyf.authorization.authorization.api.dto.AuthorizationGrantTypeValue;
import dev.madbyf.authorization.authorization.api.dto.ClientAuthenticationMethodType;
import dev.madbyf.authorization.authorization.api.dto.ClientRegistrationRequest;
import dev.madbyf.authorization.authorization.api.dto.ClientResponse;
import dev.madbyf.authorization.authorization.api.dto.ClientSearchCriteria;
import dev.madbyf.authorization.authorization.api.dto.ClientUpdateRequest;
import dev.madbyf.authorization.authorization.domain.model.Client;
import dev.madbyf.authorization.authorization.domain.repository.ClientRepository;
import dev.madbyf.authorization.authorization.domain.repository.utils.ClientSpecifications;
import dev.madbyf.authorization.authorization.domain.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void create(ClientRegistrationRequest request) {
        var clientId = requireNotBlank(request.clientId(), "clientId");
        if (clientRepository.existsByClientId(clientId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "clientId is already in use");
        }

        var authMethods = requireNotEmpty(request.clientAuthenticationMethods(), "clientAuthenticationMethods");
        var grantTypes = requireNotEmpty(request.authorizationGrantTypes(), "authorizationGrantTypes");
        var scopes = requireStringSet(request.scopes(), "scopes");
        var redirectUris = normalizeStringSet(request.redirectUris(), "redirectUris");
        var postLogoutRedirectUris = normalizeStringSet(request.postLogoutRedirectUris(), "postLogoutRedirectUris");

        validateClient(authMethods, grantTypes, scopes, redirectUris);
        var clientSecret = resolveClientSecretForCreate(authMethods, request.clientSecret());

        var registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientIdIssuedAt(Instant.now())
                .clientSecret(clientSecret)
                .clientSecretExpiresAt(request.clientSecretExpiresAt())
                .clientName(requireNotBlank(request.clientName(), "clientName"))
                .clientAuthenticationMethods(methods -> replaceClientAuthenticationMethods(methods, authMethods))
                .authorizationGrantTypes(types -> replaceAuthorizationGrantTypes(types, grantTypes))
                .redirectUris(values -> replaceStrings(values, redirectUris))
                .postLogoutRedirectUris(values -> replaceStrings(values, postLogoutRedirectUris))
                .scopes(values -> replaceStrings(values, scopes))
                .clientSettings(buildClientSettings(request))
                .tokenSettings(buildTokenSettings(request))
                .build();

        registeredClientRepository.save(registeredClient);
    }

    @Override
    public void update(String id, ClientUpdateRequest request) {
        var current = findRegisteredClient(id);

        var clientId = request.clientId() == null ? current.getClientId() : requireNotBlank(request.clientId(), "clientId");
        if (!clientId.equals(current.getClientId()) && clientRepository.existsByClientIdAndIdNot(clientId, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "clientId is already in use");
        }

        var authMethods = request.clientAuthenticationMethods() == null
                ? current.getClientAuthenticationMethods().stream()
                        .map(ClientAuthenticationMethodType::from)
                        .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new))
                : requireNotEmpty(request.clientAuthenticationMethods(), "clientAuthenticationMethods");
        var grantTypes = request.authorizationGrantTypes() == null
                ? current.getAuthorizationGrantTypes().stream()
                        .map(AuthorizationGrantTypeValue::from)
                        .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new))
                : requireNotEmpty(request.authorizationGrantTypes(), "authorizationGrantTypes");
        var scopes = request.scopes() == null
                ? copyStrings(current.getScopes())
                : requireStringSet(request.scopes(), "scopes");
        var redirectUris = request.redirectUris() == null
                ? copyStrings(current.getRedirectUris())
                : normalizeStringSet(request.redirectUris(), "redirectUris");
        var postLogoutRedirectUris = request.postLogoutRedirectUris() == null
                ? copyStrings(current.getPostLogoutRedirectUris())
                : normalizeStringSet(request.postLogoutRedirectUris(), "postLogoutRedirectUris");

        validateClient(authMethods, grantTypes, scopes, redirectUris);

        var registeredClient = RegisteredClient.withId(current.getId())
                .clientId(clientId)
                .clientIdIssuedAt(current.getClientIdIssuedAt())
                .clientSecret(resolveClientSecretForUpdate(current, authMethods, request.clientSecret()))
                .clientSecretExpiresAt(request.clientSecretExpiresAt() == null
                        ? current.getClientSecretExpiresAt()
                        : request.clientSecretExpiresAt())
                .clientName(request.clientName() == null
                        ? current.getClientName()
                        : requireNotBlank(request.clientName(), "clientName"))
                .clientAuthenticationMethods(methods -> replaceClientAuthenticationMethods(methods, authMethods))
                .authorizationGrantTypes(types -> replaceAuthorizationGrantTypes(types, grantTypes))
                .redirectUris(values -> replaceStrings(values, redirectUris))
                .postLogoutRedirectUris(values -> replaceStrings(values, postLogoutRedirectUris))
                .scopes(values -> replaceStrings(values, scopes))
                .clientSettings(buildClientSettings(current, request))
                .tokenSettings(buildTokenSettings(current, request))
                .build();

        registeredClientRepository.save(registeredClient);
    }

    @Override
    public void delete(String id) {
        if (!clientRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client not found");
        }
        clientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponse get(String id) {
        return toResponse(findRegisteredClient(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientResponse> getAll(ClientSearchCriteria criteria, Pageable pageable) {
        return clientRepository.findAll(
            ClientSpecifications.search(criteria),
            pageable
        ).map(this::toResponse);
        // return clientRepository.search(
        //         trimToNull(criteria.clientId()),
        //         trimToNull(criteria.clientName()),
        //         criteria.clientAuthenticationMethod() == null ? null : criteria.clientAuthenticationMethod().value(),
        //         criteria.authorizationGrantType() == null ? null : criteria.authorizationGrantType().value(),
        //         trimToNull(criteria.scope()),
        //         pageable
        // ).map(this::toResponse);
    }

    private RegisteredClient findRegisteredClient(String id) {
        var registeredClient = registeredClientRepository.findById(id);
        if (registeredClient == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client not found");
        }
        return registeredClient;
    }

    private ClientResponse toResponse(Client client) {
        return toResponse(findRegisteredClient(client.getId()));
    }

    private ClientResponse toResponse(RegisteredClient client) {
        return new ClientResponse(
                client.getId(),
                client.getClientId(),
                client.getClientIdIssuedAt(),
                client.getClientSecretExpiresAt(),
                client.getClientName(),
                client.getClientAuthenticationMethods().stream()
                        .map(ClientAuthenticationMethodType::from)
                        .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new)),
                client.getAuthorizationGrantTypes().stream()
                        .map(AuthorizationGrantTypeValue::from)
                        .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new)),
                copyStrings(client.getRedirectUris()),
                copyStrings(client.getPostLogoutRedirectUris()),
                copyStrings(client.getScopes()),
                client.getClientSettings().isRequireProofKey(),
                client.getClientSettings().isRequireAuthorizationConsent(),
                client.getTokenSettings().isReuseRefreshTokens(),
                client.getTokenSettings().getAuthorizationCodeTimeToLive().toSeconds(),
                client.getTokenSettings().getAccessTokenTimeToLive().toSeconds(),
                client.getTokenSettings().getRefreshTokenTimeToLive().toSeconds(),
                client.getTokenSettings().getDeviceCodeTimeToLive().toSeconds()
        );
    }

    private ClientSettings buildClientSettings(ClientRegistrationRequest request) {
        return ClientSettings.builder()
                .requireProofKey(Boolean.TRUE.equals(request.requireProofKey()))
                .requireAuthorizationConsent(Boolean.TRUE.equals(request.requireAuthorizationConsent()))
                .build();
    }

    private ClientSettings buildClientSettings(RegisteredClient current, ClientUpdateRequest request) {
        var builder = ClientSettings.withSettings(new java.util.HashMap<>(current.getClientSettings().getSettings()));
        if (request.requireProofKey() != null) {
            builder.requireProofKey(request.requireProofKey());
        }
        if (request.requireAuthorizationConsent() != null) {
            builder.requireAuthorizationConsent(request.requireAuthorizationConsent());
        }
        return builder.build();
    }

    private TokenSettings buildTokenSettings(ClientRegistrationRequest request) {
        var builder = TokenSettings.builder();
        applyTokenSetting(request.reuseRefreshTokens(), builder::reuseRefreshTokens);
        applyTokenSetting(request.authorizationCodeTimeToLiveSeconds(), seconds -> builder.authorizationCodeTimeToLive(Duration.ofSeconds(seconds)));
        applyTokenSetting(request.accessTokenTimeToLiveSeconds(), seconds -> builder.accessTokenTimeToLive(Duration.ofSeconds(seconds)));
        applyTokenSetting(request.refreshTokenTimeToLiveSeconds(), seconds -> builder.refreshTokenTimeToLive(Duration.ofSeconds(seconds)));
        applyTokenSetting(request.deviceCodeTimeToLiveSeconds(), seconds -> builder.deviceCodeTimeToLive(Duration.ofSeconds(seconds)));
        return builder.build();
    }

    private TokenSettings buildTokenSettings(RegisteredClient current, ClientUpdateRequest request) {
        var builder = TokenSettings.withSettings(new java.util.HashMap<>(current.getTokenSettings().getSettings()));
        applyTokenSetting(request.reuseRefreshTokens(), builder::reuseRefreshTokens);
        applyTokenSetting(request.authorizationCodeTimeToLiveSeconds(), seconds -> builder.authorizationCodeTimeToLive(Duration.ofSeconds(seconds)));
        applyTokenSetting(request.accessTokenTimeToLiveSeconds(), seconds -> builder.accessTokenTimeToLive(Duration.ofSeconds(seconds)));
        applyTokenSetting(request.refreshTokenTimeToLiveSeconds(), seconds -> builder.refreshTokenTimeToLive(Duration.ofSeconds(seconds)));
        applyTokenSetting(request.deviceCodeTimeToLiveSeconds(), seconds -> builder.deviceCodeTimeToLive(Duration.ofSeconds(seconds)));
        return builder.build();
    }

    private <T> void applyTokenSetting(T value, Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    private String resolveClientSecretForCreate(Set<ClientAuthenticationMethodType> authMethods, String clientSecret) {
        if (authMethods.contains(ClientAuthenticationMethodType.NONE)) {
            return null;
        }
        if (requiresClientSecret(authMethods)) {
            return passwordEncoder.encode(requireNotBlank(clientSecret, "clientSecret"));
        }
        return trimToNull(clientSecret) == null ? null : passwordEncoder.encode(clientSecret.trim());
    }

    private String resolveClientSecretForUpdate(
            RegisteredClient current,
            Set<ClientAuthenticationMethodType> authMethods,
            String requestedSecret
    ) {
        if (authMethods.contains(ClientAuthenticationMethodType.NONE)) {
            return null;
        }
        if (requestedSecret != null) {
            return passwordEncoder.encode(requireNotBlank(requestedSecret, "clientSecret"));
        }
        if (requiresClientSecret(authMethods) && !StringUtils.hasText(current.getClientSecret())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "clientSecret is required for selected authentication methods");
        }
        return current.getClientSecret();
    }

    private boolean requiresClientSecret(Set<ClientAuthenticationMethodType> authMethods) {
        return authMethods.contains(ClientAuthenticationMethodType.CLIENT_SECRET_BASIC)
                || authMethods.contains(ClientAuthenticationMethodType.CLIENT_SECRET_POST)
                || authMethods.contains(ClientAuthenticationMethodType.CLIENT_SECRET_JWT);
    }

    private void validateClient(
            Set<ClientAuthenticationMethodType> authMethods,
            Set<AuthorizationGrantTypeValue> grantTypes,
            Set<String> scopes,
            Set<String> redirectUris
    ) {
        if (authMethods.contains(ClientAuthenticationMethodType.NONE) && authMethods.size() > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NONE cannot be combined with other client authentication methods");
        }
        if (grantTypes.contains(AuthorizationGrantTypeValue.AUTHORIZATION_CODE) && redirectUris.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "redirectUris are required for AUTHORIZATION_CODE clients");
        }
        if (scopes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "scopes are required");
        }
    }

    private void replaceClientAuthenticationMethods(
            Set<ClientAuthenticationMethod> target,
            Set<ClientAuthenticationMethodType> values
    ) {
        target.clear();
        values.stream()
                .map(ClientAuthenticationMethodType::toSpringValue)
                .forEach(target::add);
    }

    private void replaceAuthorizationGrantTypes(
            Set<AuthorizationGrantType> target,
            Set<AuthorizationGrantTypeValue> values
    ) {
        target.clear();
        values.stream()
                .map(AuthorizationGrantTypeValue::toSpringValue)
                .forEach(target::add);
    }

    private void replaceStrings(Set<String> target, Set<String> values) {
        target.clear();
        target.addAll(values);
    }

    private <T> Set<T> requireNotEmpty(Set<T> values, String field) {
        if (values == null || values.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " is required");
        }
        return new LinkedHashSet<>(values);
    }

    private Set<String> requireStringSet(Set<String> values, String field) {
        var normalized = normalizeStringSet(values, field);
        if (normalized.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " is required");
        }
        return normalized;
    }

    private Set<String> normalizeStringSet(Set<String> values, String field) {
        if (values == null || values.isEmpty()) {
            return new LinkedHashSet<>();
        }

        var normalized = new LinkedHashSet<String>();
        for (var value : values) {
            var trimmed = trimToNull(value);
            if (trimmed == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " cannot contain blank values");
            }
            normalized.add(trimmed);
        }
        return normalized;
    }

    private Set<String> copyStrings(Set<String> values) {
        if (values == null || values.isEmpty()) {
            return new LinkedHashSet<>();
        }
        return new LinkedHashSet<>(values);
    }

    private String requireNotBlank(String value, String field) {
        var trimmed = trimToNull(value);
        if (trimmed == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " is required");
        }
        return trimmed;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        var trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
