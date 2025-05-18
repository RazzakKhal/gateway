package com.bookNDrive.gateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomSecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtUtil jwtUtil;

    public CustomSecurityContextRepository(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty(); // Pas de session persistée
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);

            try {
                if (!jwtUtil.isTokenExpired(jwt)) {
                    String username = jwtUtil.extractUsername(jwt);
                    Claims claims = jwtUtil.extractAllClaims(jwt); // méthode à implémenter


                        List<String> roles = claims.get("roles", List.class);

                        Collection<GrantedAuthority> authorities = roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());

                        var auth = new UsernamePasswordAuthenticationToken(username, jwt,authorities);
                        return Mono.just(new SecurityContextImpl(auth));
                }
            } catch (Exception e) {
                System.out.println("❌ Token invalide dans la gateway : " + e.getMessage());
            }
        }

        return Mono.empty(); // pas de token ou invalide
    }
}
