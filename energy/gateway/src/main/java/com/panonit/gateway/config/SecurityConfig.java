package com.panonit.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll();
                            auth.requestMatchers(HttpMethod.GET, "/api-docs/*/**").permitAll();
                            auth.requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/swagger-config").permitAll();
                            auth.anyRequest().authenticated();
                        }
                )
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(Customizer.withDefaults())
                )
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(@Value("${keycloak.auth.jwk-set-uri}") String url) {
        return NimbusJwtDecoder.withJwkSetUri(url).build();
    }
}
