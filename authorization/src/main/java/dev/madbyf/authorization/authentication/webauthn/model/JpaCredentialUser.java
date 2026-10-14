package dev.madbyf.authorization.authentication.webauthn.model;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "credential_user")
public class JpaCredentialUser implements PublicKeyCredentialUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(
        name = "user_id",
        nullable = false,
        unique = true,
        columnDefinition = "bytea"
    )
    private byte[] userId;

    @Column(name = "display_name")
    private String displayName;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Bytes getId() {
        return new Bytes(this.userId);
    }

    @Override
    public @Nullable String getDisplayName() {
        return this.displayName;
    }
}
