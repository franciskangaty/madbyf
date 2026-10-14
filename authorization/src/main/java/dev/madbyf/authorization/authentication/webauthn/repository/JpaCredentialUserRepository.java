package dev.madbyf.authorization.authentication.webauthn.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.madbyf.authorization.authentication.webauthn.model.JpaCredentialUser;

public interface JpaCredentialUserRepository
		extends JpaRepository<JpaCredentialUser, UUID> {

	Optional<JpaCredentialUser> findByUserId(byte[] userId);

	Optional<JpaCredentialUser> findByName(String name);
}
