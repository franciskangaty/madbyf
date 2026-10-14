package dev.madbyf.authorization.authorization.api.dto;

public record ClientSearchCriteria(
        String clientId,
        String clientName,
        ClientAuthenticationMethodType clientAuthenticationMethod,
        AuthorizationGrantTypeValue authorizationGrantType,
        String scope
) {
}
