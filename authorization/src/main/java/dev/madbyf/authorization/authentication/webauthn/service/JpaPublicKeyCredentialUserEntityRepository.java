package dev.madbyf.authorization.authentication.webauthn.service;

import org.jspecify.annotations.Nullable;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository;
import org.springframework.stereotype.Component;

import dev.madbyf.authorization.authentication.webauthn.model.JpaCredentialUser;
import dev.madbyf.authorization.authentication.webauthn.repository.JpaCredentialUserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JpaPublicKeyCredentialUserEntityRepository
		implements PublicKeyCredentialUserEntityRepository {

	private final JpaCredentialUserRepository repository;

	@Override
	public @Nullable PublicKeyCredentialUserEntity findById(Bytes id) {
		return repository.findByUserId(id.getBytes())
				.orElse(null);
	}

	@Override
	public @Nullable PublicKeyCredentialUserEntity findByUsername(String username) {
		return repository.findByName(username)
				.orElse(null);
	}

	@Override
	public void save(PublicKeyCredentialUserEntity userEntity) {
		JpaCredentialUser user = new JpaCredentialUser();

		user.setUserId(userEntity.getId().getBytes());
		user.setName(userEntity.getName());
		user.setDisplayName(userEntity.getDisplayName());

		repository.save(user);
	}

	@Override
	public void delete(Bytes id) {
		repository.findByUserId(id.getBytes())
				.ifPresent(repository::delete);
	}
}
