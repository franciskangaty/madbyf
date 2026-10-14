package dev.madbyf.authorization.authorization.api.dto;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

public enum AuthorizationGrantTypeValue {
    AUTHORIZATION_CODE(AuthorizationGrantType.AUTHORIZATION_CODE),
    REFRESH_TOKEN(AuthorizationGrantType.REFRESH_TOKEN),
    CLIENT_CREDENTIALS(AuthorizationGrantType.CLIENT_CREDENTIALS),
    JWT_BEARER(AuthorizationGrantType.JWT_BEARER),
    DEVICE_CODE(AuthorizationGrantType.DEVICE_CODE),
    TOKEN_EXCHANGE(AuthorizationGrantType.TOKEN_EXCHANGE);

    private final AuthorizationGrantType springValue;

    AuthorizationGrantTypeValue(AuthorizationGrantType springValue) {
        this.springValue = springValue;
    }

    public AuthorizationGrantType toSpringValue() {
        return springValue;
    }

    public String value() {
        return springValue.getValue();
    }

    public static AuthorizationGrantTypeValue from(AuthorizationGrantType grantType) {
        for (var type : values()) {
            if (type.springValue.equals(grantType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported authorization grant type: " + grantType.getValue());
    }

    public static AuthorizationGrantTypeValue fromValue(String value) {
        for (var type : values()) {
            if (type.value().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported authorization grant type: " + value);
    }
}
