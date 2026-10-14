package dev.madbyf.authorization.user.config;

import dev.madbyf.authorization.user.domain.model.User;
import dev.madbyf.authorization.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class BootStrapApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${bootstrap.superuser.username}")
    private String username;

    @Value("${bootstrap.superuser.password}")
    private String password;

    @Value("${bootstrap.superuser.first-name}")
    private String firstName;

    @Value("${bootstrap.superuser.last-name}")
    private String lastName;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.existsByRoles("SUPERUSER")) {
            return;
        }

        var superuser = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .roles(Set.of("SUPERUSER"))
                .enabled(true)
                .verified(true)
                .build();

        userRepository.save(superuser);

    	return;
    }
}
