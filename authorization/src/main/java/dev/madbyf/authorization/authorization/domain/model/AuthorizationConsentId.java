package dev.madbyf.authorization.authorization.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationConsentId implements Serializable {
    private String registeredClientId;
    private String principalName;
}
