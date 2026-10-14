package dev.madbyf.authorization.authentication.webauthn.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.web.webauthn.api.AuthenticatorTransport;

import dev.madbyf.authorization.user.domain.model.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "webauthn_credentials")
public class WebAuthnCredential {

    @Id
    @Column(name = "credential_id", nullable = false)
    private String id;

    @Column(name = "user_entity_user_id", nullable = false)
    private String userEntityUserId;

/*    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;*/

    @Lob
    @Column(name = "public_key_cose", nullable = false)
    private byte[] publicKeyCose;

    @Column(nullable = false)
    private long signatureCount;

    @Column(nullable = false)
    private boolean uvInitialized;

    @Column(nullable = false)
    private boolean backupEligible;

    @Column(nullable = false)
    private boolean backupState;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "webauthn_credential_transports",
            joinColumns = @JoinColumn(name = "credential_id"))
    @Column(name = "transport")
    @Enumerated(EnumType.STRING)
    private Set<AuthenticatorTransport> transports = new HashSet<>();

    @Lob
    private byte[] attestationObject;

    @Lob
    private byte[] attestationClientDataJson;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private Instant created;

    @Column(nullable = false)
    private Instant lastUsed;
}
