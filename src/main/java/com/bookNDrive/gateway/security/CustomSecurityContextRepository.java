package com.bookNDrive.gateway.security;

import com.bookNDrive.gateway.services.TokenValidatorService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class CustomSecurityContextRepository implements ServerSecurityContextRepository {

    private final TokenValidatorService tokenValidatorService;

    public CustomSecurityContextRepository(TokenValidatorService tokenValidatorService) {
        this.tokenValidatorService = tokenValidatorService;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty(); // Pas de session persistée
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {
            return tokenValidatorService.validate(token)
                    .map(userInfo -> {
                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                userInfo.get("mail"),
                                null,
                                Collections.emptyList() // Tu peux ajouter les rôles ici
                        );
                        return new SecurityContextImpl(auth);
                    });
        }

        return Mono.empty(); // Aucun utilisateur
    }
}
