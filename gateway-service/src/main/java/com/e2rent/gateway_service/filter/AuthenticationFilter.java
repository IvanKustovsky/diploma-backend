package com.e2rent.gateway_service.filter;

import com.e2rent.gateway_service.exception.MissingAuthorizationHeaderException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                String authHeader = extractAuthHeader(exchange);

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new MissingAuthorizationHeaderException("Missing or invalid authorization header");
                }
            }
            return chain.filter(exchange);
        };
    }

    private String extractAuthHeader(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    }

    public static class Config {
    }
}
