package dev.madbyf.authorization.authorization.api.dto;

import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public enum ClientAuthenticationMethodType {
    CLIENT_SECRET_BASIC(ClientAuthenticationMethod.CLIENT_SECRET_BASIC),
    CLIENT_SECRET_POST(ClientAuthenticationMethod.CLIENT_SECRET_POST),
    CLIENT_SECRET_JWT(ClientAuthenticationMethod.CLIENT_SECRET_JWT),
    PRIVATE_KEY_JWT(ClientAuthenticationMethod.PRIVATE_KEY_JWT),
    NONE(ClientAuthenticationMethod.NONE),
    TLS_CLIENT_AUTH(ClientAuthenticationMethod.TLS_CLIENT_AUTH),
    SELF_SIGNED_TLS_CLIENT_AUTH(ClientAuthenticationMethod.SELF_SIGNED_TLS_CLIENT_AUTH);

    private final ClientAuthenticationMethod springValue;

    ClientAuthenticationMethodType(ClientAuthenticationMethod springValue) {
        this.springValue = springValue;
    }

    public ClientAuthenticationMethod toSpringValue() {
        return springValue;
    }

    public String value() {
        return springValue.getValue();
    }

    public static ClientAuthenticationMethodType from(ClientAuthenticationMethod method) {
        for (var type : values()) {
            if (type.springValue.equals(method)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported client authentication method: " + method.getValue());
    }

    public static ClientAuthenticationMethodType fromValue(String value) {
        for (var type : values()) {
            if (type.value().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported client authentication method: " + value);
    }
}
