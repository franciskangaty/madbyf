package dev.madbyf.authorization.authentication.webauthn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "passkey_credentials")
public class PasskeyCredentialEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String credentialId;
    private String userId;

    @Lob
    private byte[] publicKey;
    private long signatureCount;
    private String label;
    private Instant created;
    private Instant lastUsed;
}
