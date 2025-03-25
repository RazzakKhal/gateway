package com.bookNDrive.gateway.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class TokenValidatorService {

    private final WebClient webClient;

    public TokenValidatorService(WebClient.Builder builder, @Value("${user-service.url}") String uri) {
        System.out.println("l'uri : " + uri);
        this.webClient = builder
                .baseUrl(uri)
                .build();
    }

    public Mono<Map<String, String>> validate(String token) {
        return webClient.get()
                .uri("/users/validate")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .doOnNext(map -> System.out.println("✅ Réponse du user-service : " + map))
                .doOnError(e -> System.out.println("❌ Erreur de validation : " + e.getMessage()));
    }
}
