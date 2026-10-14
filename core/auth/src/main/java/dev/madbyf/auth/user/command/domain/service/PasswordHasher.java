package dev.madbyf.auth.user.command.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class PasswordHasher {
    private final PasswordEncoder passwordEncoder;

    public String hash(String password) {
        return passwordEncoder.encode(password);
    }
    
}
