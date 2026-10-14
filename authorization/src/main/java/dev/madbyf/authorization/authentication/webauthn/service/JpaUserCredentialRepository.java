package dev.madbyf.authorization.authentication.webauthn.service;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.CredentialRecord;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.stereotype.Component;

import dev.madbyf.authorization.authentication.webauthn.model.JpaCredentialRecord;
import dev.madbyf.authorization.authentication.webauthn.repository.JpaCredentialRecordRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JpaUserCredentialRepository implements UserCredentialRepository {

	private final JpaCredentialRecordRepository repository;

	@Override
	public void delete(Bytes credentialId) {
		repository.findByCredentialId(credentialId.getBytes())
				.ifPresent(repository::delete);
	}

	@Override
	public void save(CredentialRecord credentialRecord) {
		JpaCredentialRecord entity = repository
				.findByCredentialId(
						credentialRecord.getCredentialId().getBytes()
				)
				.orElseGet(JpaCredentialRecord::new);

		entity.setCredentialType(credentialRecord.getCredentialType());
		entity.setCredentialId(
				credentialRecord.getCredentialId().getBytes()
		);
		entity.setPublicKey(
				credentialRecord.getPublicKey().getBytes()
		);
		entity.setSignatureCount(
				credentialRecord.getSignatureCount()
		);
		entity.setUvInitialized(
				credentialRecord.isUvInitialized()
		);
		entity.setTransports(
				credentialRecord.getTransports()
		);
		entity.setBackupEligible(
				credentialRecord.isBackupEligible()
		);
		entity.setBackupState(
				credentialRecord.isBackupState()
		);
		entity.setUserEntityUserId(
				credentialRecord.getUserEntityUserId().getBytes()
		);
		entity.setLabel(
				credentialRecord.getLabel()
		);
		entity.setLastUsed(
				credentialRecord.getLastUsed()
		);
		entity.setCreated(
				credentialRecord.getCreated()
		);

		if (credentialRecord.getAttestationObject() != null) {
			entity.setAttestationObject(
					credentialRecord.getAttestationObject().getBytes()
			);
		}

		if (credentialRecord.getAttestationClientDataJSON() != null) {
			entity.setAttestationClientDataJSON(
					credentialRecord.getAttestationClientDataJSON().getBytes()
			);
		}

		repository.save(entity);
	}

	@Override
	public @Nullable CredentialRecord findByCredentialId(Bytes credentialId) {
		return repository.findByCredentialId(credentialId.getBytes())
				.orElse(null);
	}

	@Override
	public List<CredentialRecord> findByUserId(Bytes userId) {
		return List.copyOf(
				repository.findByUserEntityUserId(userId.getBytes())
		);
	}
}
