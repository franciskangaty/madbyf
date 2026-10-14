package dev.madbyf.authorization.authorization.domain.service;

import dev.madbyf.authorization.authorization.domain.model.AuthorizationConsent;
import dev.madbyf.authorization.authorization.domain.repository.AuthorizationConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class JpaOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {
    private final AuthorizationConsentRepository authorizationConsentRepository;
    private final RegisteredClientRepository registeredClientRepository;

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        authorizationConsentRepository.save(toEntity(authorizationConsent));
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        authorizationConsentRepository.deleteByRegisteredClientIdAndPrincipalName(
                authorizationConsent.getRegisteredClientId(),
                authorizationConsent.getPrincipalName()
        );
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return authorizationConsentRepository.findByRegisteredClientIdAndPrincipalName(registeredClientId, principalName)
                .map(this::toObject)
                .orElse(null);
    }

    private OAuth2AuthorizationConsent toObject(AuthorizationConsent authorizationConsent) {
        var registeredClientId = authorizationConsent.getRegisteredClientId();
        var registeredClient = registeredClientRepository.findById(registeredClientId);
        if (registeredClient == null) {
            throw new DataRetrievalFailureException(
                    "The RegisteredClient with id '" + registeredClientId + "' was not found.");
        }

        var builder = OAuth2AuthorizationConsent.withId(
                registeredClientId,
                authorizationConsent.getPrincipalName()
        );
        if (StringUtils.hasText(authorizationConsent.getAuthorities())) {
            for (var authority : StringUtils.commaDelimitedListToSet(authorizationConsent.getAuthorities())) {
                builder.authority(new SimpleGrantedAuthority(authority));
            }
        }
        return builder.build();
    }

    private AuthorizationConsent toEntity(OAuth2AuthorizationConsent authorizationConsent) {
        var entity = new AuthorizationConsent();
        entity.setRegisteredClientId(authorizationConsent.getRegisteredClientId());
        entity.setPrincipalName(authorizationConsent.getPrincipalName());

        var authorities = new HashSet<String>();
        for (GrantedAuthority authority : authorizationConsent.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
        entity.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorities));
        return entity;
    }
}
