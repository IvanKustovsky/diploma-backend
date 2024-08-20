package com.example.gatewayservice;

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
        public RouteLocator e2rentRouteConfig(RouteLocatorBuilder builder) {
            return builder.routes()
                    .route(p -> p
                            .path("/e2rent/users/**")
                            .filters(f -> f.rewritePath("/e2rent/users/(?<segment>.*)", "/${segment}"))
                            .uri("lb://USERS"))
                    .route(p -> p
                            .path("/e2rent/equipments/**")
                            .filters(f -> f.rewritePath("/e2rent/equipments/(?<segment>.*)", "/${segment}"))
                            .uri("lb://EQUIPMENTS"))
                    .build();
        }
}
