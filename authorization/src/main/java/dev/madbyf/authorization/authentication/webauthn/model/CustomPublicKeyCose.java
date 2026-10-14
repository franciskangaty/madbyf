package dev.madbyf.authorization.authentication.webauthn.model;

import org.springframework.security.web.webauthn.api.PublicKeyCose;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomPublicKeyCose implements PublicKeyCose{
	private byte[] publicKey;

	@Override
	public byte[] getBytes() {
		return publicKey;
	}

	
}