package dev.madbyf.authorization.authentication.webauthn.service;

import org.springframework.security.web.webauthn.api.PublicKeyCredentialType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PublicKeyCredentialTypeConverter
		implements AttributeConverter<PublicKeyCredentialType, String> {

	@Override
	public String convertToDatabaseColumn(PublicKeyCredentialType attribute) {
		return attribute == null ? null : attribute.getValue();
	}

	@Override
	public PublicKeyCredentialType convertToEntityAttribute(String value) {
		return value == null ? null : PublicKeyCredentialType.valueOf(value);
	}
}
