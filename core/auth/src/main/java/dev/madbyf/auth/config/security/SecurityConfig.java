package dev.madbyf.auth.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC_URL = {
            "/login",
            "/register",
            "/user/register",
            "/api/v1/users/register",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/error",
            "/",
            "/css/**",
            "/js/**",
            "/images/**"
    };

    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf->csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(auth->
                        auth
                                .requestMatchers(PUBLIC_URL).permitAll()
                                .requestMatchers("/**").hasRole("SUPERUSER")
                                .anyRequest().authenticated()
                )
                // .formLogin(form->
                //         form
                //                 .loginPage("/login")
                //                 .permitAll()
                // )
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                // .oneTimeTokenLogin(withDefaults())
                // .webAuthn(webAuthn->
                //         webAuthn
                //                 .disableDefaultRegistrationPage(false)
                //                 .allowedOrigins("http://localhost:8080")
                //                 .rpName("MadByF Auth")
                //                 .rpId("localhost")
                // )
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
