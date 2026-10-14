package dev.madbyf.authorization.authentication.webauthn.repository;

import dev.madbyf.authorization.authentication.webauthn.model.PasskeyUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasskeyUserRepository extends JpaRepository<PasskeyUserEntity, String> {
    Optional<PasskeyUserEntity> findByUsername(String username);
}