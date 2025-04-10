package com.bookNDrive.gateway.security;

import com.bookNDrive.gateway.services.TokenValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsConfigurationSource;


import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfig{

    @Autowired
    TokenValidatorService tokenValidatorService;

    @Value("${web.url}") String webUrl;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, CustomSecurityContextRepository securityContextRepository, CorsConfigurationSource corsConfigSource) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigSource))
                .securityContextRepository(securityContextRepository)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/user-service/users/signin", "/user-service/users/signup", "/user-service/users/validate", "/formula-service/test", "/actuator/**").permitAll()
                        .anyExchange().permitAll()
                )
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(webUrl)); // ou ton front
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // si tu as besoin de cookies/token auth

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}