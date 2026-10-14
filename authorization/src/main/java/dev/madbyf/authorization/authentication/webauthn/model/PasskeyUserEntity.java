package dev.madbyf.authorization.authentication.webauthn.model;

import dev.madbyf.authorization.config.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "passkey_credentials")
public class PasskeyUserEntity {
    @Id
    private String id;
    private String username;
    private String displayName;
}
