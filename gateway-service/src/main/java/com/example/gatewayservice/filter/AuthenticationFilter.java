package com.example.gatewayservice.filter;

import com.example.gatewayservice.exception.MissingAuthorizationHeaderException;
import com.example.gatewayservice.service.client.IdentityFeignClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;

    private final IdentityFeignClient identityFeignClient;

    public AuthenticationFilter(RouteValidator validator,
                                @Lazy IdentityFeignClient identityFeignClient) {
        this.validator = validator;
        this.identityFeignClient = identityFeignClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                String authHeader = extractAuthHeader(exchange);

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new MissingAuthorizationHeaderException("Missing or invalid authorization header");
                }

                String token = authHeader.substring(7);
                identityFeignClient.validateToken(token);
            }
            return chain.filter(exchange);
        };
    }

    private String extractAuthHeader(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    }

    public static class Config { }
}
