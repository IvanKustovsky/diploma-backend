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
                                .rewritePath("/e2rent/(?<segment>.*)", "/${segment}")
                                .filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://USERS"))
                .route(p -> p
                        .path("/e2rent/equipments/**", "/e2rent/advertisement/**")
                        .filters(f -> f
                                .rewritePath("/e2rent/(?<segment>.*)", "/${segment}")
                                .filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://EQUIPMENTS"))
                .route(p -> p
                        .path("/e2rent/auth/**")
                        .filters(f -> f
                                .rewritePath("/e2rent/(?<segment>.*)", "/${segment}"))
                        .uri("lb://AUTH-SERVICE"))
                .route(p -> p
                        .path("/e2rent/rentals/**")
                        .filters(f -> f
                                .rewritePath("/e2rent/(?<segment>.*)", "/${segment}"))
                        .uri("lb://RENT-SERVICE"))
                .build();
    }
}
