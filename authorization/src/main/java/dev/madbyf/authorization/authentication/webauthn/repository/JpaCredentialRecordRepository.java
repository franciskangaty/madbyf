package dev.madbyf.authorization.authentication.webauthn.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.madbyf.authorization.authentication.webauthn.model.JpaCredentialRecord;

public interface JpaCredentialRecordRepository
		extends JpaRepository<JpaCredentialRecord, UUID> {

	Optional<JpaCredentialRecord> findByCredentialId(byte[] credentialId);

	List<JpaCredentialRecord> findByUserEntityUserId(byte[] userId);
}
