package dev.madbyf.authorization.authentication.token.model;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.authentication.ott.OneTimeToken;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "one_time_tokens")
public class JpaOneTimeToken implements OneTimeToken {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String token;
    private String username;
    private Instant createdAt;
    private Instant expiresAt;
    private boolean used;

    @Override
    public String getTokenValue() {
        return token;
    }
}
