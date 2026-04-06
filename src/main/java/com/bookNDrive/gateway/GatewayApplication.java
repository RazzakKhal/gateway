package com.bookNDrive.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routeConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(path -> path
                        .path("/user-service/**")
                        .filters(f -> f
                                .rewritePath("/user-service/(?<segment>.*)", "/${segment}")
                                .circuitBreaker(config -> config
                                        .setName("UserServiceCircuitBraker")
                                        .setFallbackUri("/fallback/test")

                                )

                        )
                        .uri("lb://USER-SERVICE")

                )
                .route(path -> path
                        .path("/formula-service/**")
                        .filters(f -> f
                                .rewritePath("/formula-service/(?<segment>.*)", "/${segment}")
                                .circuitBreaker(config -> config
                                        .setName("FormulaServiceCircuitBraker")
                                        .setFallbackUri("/fallback/test")
                                )

                        )
                        .uri("lb://FORMULA-SERVICE")
                )
                .route(path -> path
                        .path("/payment-service/**")
                        .filters(f -> f
                                .rewritePath("/payment-service/(?<segment>.*)", "/${segment}")
                                .circuitBreaker(config -> config
                                        .setName("PaymentServiceCircuitBraker")
                                        .setFallbackUri("/fallback/test")

                                )

                        )
                        .uri("lb://PAYMENT-SERVICE")
                )
                .route(path -> path
                        .path("/notification-service/**")
                        .filters(f -> f
                                .rewritePath("/notification-service/(?<segment>.*)", "/${segment}")
                                .circuitBreaker(config ->
                                        config
                                                .setName("NotificationServiceCircuitBraker")
                                                .setFallbackUri("/fallback/test")

                                )

                        )
                        .uri("lb://NOTIFICATION-SERVICE")
                )
                .build();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }


}
