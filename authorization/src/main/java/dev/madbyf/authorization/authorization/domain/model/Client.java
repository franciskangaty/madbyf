package dev.madbyf.authorization.authorization.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "`client`")
public class Client {
    @Id
    private String id;
    @Column(nullable = false, unique = true)
    private String clientId;
    @Column(nullable = false)
    private Instant clientIdIssuedAt;
    private String clientSecret;
    private Instant clientSecretExpiresAt;
    @Column(nullable = false)
    private String clientName;
    @Column(nullable = false, length = 1000)
    private String clientAuthenticationMethods;
    @Column(nullable = false, length = 1000)
    private String authorizationGrantTypes;
    @Column(length = 1000)
    private String redirectUris;
    @Column(length = 1000)
    private String postLogoutRedirectUris;
    @Column(nullable = false, length = 1000)
    private String scopes;
    @Column(nullable = false, length = 2000)
    private String clientSettings;
    @Column(nullable = false, length = 2000)
    private String tokenSettings;
}
