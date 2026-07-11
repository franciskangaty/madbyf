package dev.madbyf.authorization.config;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class SqlResourceHints implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(RuntimeHints hints, @Nullable ClassLoader classLoader) {
      hints.reflection().registerType(UUID.class, MemberCategory.values());
      hints.reflection().registerType(UUID[].class, MemberCategory.values());
	}

	
}