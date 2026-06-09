package dev.madbyf.authorization.security.internal;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springdoc.core.properties.SwaggerUiConfigProperties.Csrf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import static org.springframework.security.config.Customizer.*;

@Configuration
@EnableWebSecurity
public class AuthorizationConfig {

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
    @Order(1)
    SecurityFilterChain authorizationSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
        
        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                authorizationServer.oidc(withDefaults())
        );

        http
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );

        return http.build();
    }

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
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    JWKSource<SecurityContext> jwkSource() {
        var rsaKey = generateRsaKey();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    RSAKey generateRsaKey() {
        var keyPair = generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private static KeyPair generateKeyPair() {
        KeyPair keyPair;
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    // @Bean
    // public org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings authorizationServerSettings() {
    //     return org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings.builder()
    //             .authorizationEndpoint("/api/v1/oauth2/authorize")
    //             .deviceAuthorizationEndpoint("/api/v1/oauth2/device_authorization")
    //             .deviceVerificationEndpoint("/api/v1/oauth2/device_verification")
    //             .tokenEndpoint("/api/v1/oauth2/token")
    //             .tokenIntrospectionEndpoint("/api/v1/oauth2/introspect")
    //             .tokenRevocationEndpoint("/api/v1/oauth2/revoke")
    //             .jwkSetEndpoint("/api/v1/oauth2/jwks")
    //             .oidcLogoutEndpoint("/api/v1/connect/logout")
    //             .oidcUserInfoEndpoint("/api/v1/connect/userinfo")
    //             .oidcClientRegistrationEndpoint("/api/v1/connect/register")
    //             .build();
    // }

}
