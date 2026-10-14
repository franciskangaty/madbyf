package dev.madbyf.authorization.authentication.webauthn.model;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.web.webauthn.api.AuthenticatorTransport;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.CredentialRecord;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCose;
import org.springframework.security.web.webauthn.api.PublicKeyCose;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialType;

import dev.madbyf.authorization.authentication.webauthn.service.PublicKeyCredentialTypeConverter;
import dev.madbyf.authorization.config.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "credential_records")
public class JpaCredentialRecord extends BaseEntity implements CredentialRecord {

    @Column(name = "credential_type", nullable = false)
    @Convert(converter = PublicKeyCredentialTypeConverter.class)
    private PublicKeyCredentialType credentialType;

    @Column(name = "credential_id", nullable = false, columnDefinition = "bytea")
    private byte[] credentialId;

    @Column(name = "public_key", nullable = false, columnDefinition = "bytea")
    private byte[] publicKey;

    @Column(name = "signature_count", nullable = false)
    private long signatureCount;

    @Column(name = "uv_initialized", nullable = false)
    private boolean uvInitialized;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "credential_record_transports",
        joinColumns = @JoinColumn(name = "credential_record_id")
    )
    @Column(name = "transport")
    private Set<AuthenticatorTransport> transports = new HashSet<>();

    @Column(name = "backup_eligible", nullable = false)
    private boolean backupEligible;

    @Column(name = "backup_state", nullable = false)
    private boolean backupState;

    @Column(
        name = "user_entity_user_id",
        nullable = false,
        columnDefinition = "bytea"
    )
    private byte[] userEntityUserId;

    @Column(name = "attestation_object", columnDefinition = "bytea")
    private byte[] attestationObject;

    @Column(
        name = "attestation_client_data_json",
        columnDefinition = "bytea"
    )
    private byte[] attestationClientDataJSON;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "last_used", nullable = false)
    private Instant lastUsed;

    @Column(name = "created", nullable = false)
    private Instant created;

    @Override
    public PublicKeyCredentialType getCredentialType() {
        return this.credentialType;
    }

    @Override
    public Bytes getCredentialId() {
        return new Bytes(this.credentialId);
    }

    @Override
    public PublicKeyCose getPublicKey() {
        return new ImmutablePublicKeyCose(this.publicKey);
    }

    @Override
    public long getSignatureCount() {
        return this.signatureCount;
    }

    @Override
    public boolean isUvInitialized() {
        return this.uvInitialized;
    }

    @Override
    public Set<AuthenticatorTransport> getTransports() {
        if (this.transports == null) {
            return Collections.emptySet();
        }

        return Collections.unmodifiableSet(this.transports);
    }

    @Override
    public boolean isBackupEligible() {
        return this.backupEligible;
    }

    @Override
    public boolean isBackupState() {
        return this.backupState;
    }

    @Override
    public Bytes getUserEntityUserId() {
        return new Bytes(this.userEntityUserId);
    }

    @Override
    public Bytes getAttestationObject() {
        return this.attestationObject != null
            ? new Bytes(this.attestationObject)
            : null;
    }

    @Override
    public Bytes getAttestationClientDataJSON() {
        return this.attestationClientDataJSON != null
            ? new Bytes(this.attestationClientDataJSON)
            : null;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public Instant getLastUsed() {
        return this.lastUsed;
    }

    @Override
    public Instant getCreated() {
        return this.created;
    }
}
