package com.e2rent.gateway_service;

import com.e2rent.gateway_service.filter.AuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    @Bean
    public RouteLocator e2rentRouteConfig(RouteLocatorBuilder builder, AuthenticationFilter authFilter) {
        return builder.routes()
                .route(p -> p
                        .path("/e2rent/users/**")
                        .filters(f -> f
                                .rewritePath("/e2rent/users/(?<segment>.*)", "/users/${segment}")
                                .filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://USERS"))
                .route(p -> p
                        .path("/e2rent/equipments/**")
                        .filters(f -> f
                                .rewritePath("/e2rent/equipments/(?<segment>.*)", "/equipments/${segment}")
                                .filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://EQUIPMENTS"))
                .route(p -> p
                        .path("/e2rent/auth/**")
                        .filters(f -> f.rewritePath("/e2rent/auth/(?<segment>.*)", "/auth/${segment}"))
                        .uri("lb://IDENTITY-SERVICE"))
                .build();
    }
}
