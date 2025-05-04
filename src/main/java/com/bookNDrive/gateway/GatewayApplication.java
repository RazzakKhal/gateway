package com.bookNDrive.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routeConfig(RouteLocatorBuilder routeLocatorBuilder){
		return routeLocatorBuilder.routes()
				.route(path -> path
						.path("/user-service/**")
						.filters(f -> f.rewritePath("/user-service/(?<segment>.*)","/${segment}"))
						.uri("lb://USER-SERVICE")
				)
				.route(path -> path
				.path("/formula-service/**")
				.filters(f -> f.rewritePath("/formula-service/(?<segment>.*)","/${segment}"))
				.uri("lb://FORMULA-SERVICE")
		)
				.route(path -> path
						.path("/payment-service/**")
						.filters(f -> f.rewritePath("/payment-service/(?<segment>.*)","/${segment}"))
						.uri("lb://PAYMENT-SERVICE")
				)
				.build();
	}

	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}


}
