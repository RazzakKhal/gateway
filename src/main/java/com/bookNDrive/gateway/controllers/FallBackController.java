package com.bookNDrive.gateway.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallBackController {

    @GetMapping("/test")
    Mono<String> getFallBackErrorOnCircuitBreaker() {
        return Mono
                .just("une erreur est survenue lors de l'appel veuillez reessayez ultérieurement");
    }
}
