package dev.madbyf.authorization.authentication.webauthn.repository;

import dev.madbyf.authorization.authentication.webauthn.model.PasskeyCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PasskeyCredentialRepository extends JpaRepository<PasskeyCredentialEntity, String> {
    List<PasskeyCredentialEntity> findByUserId(String userId);
}