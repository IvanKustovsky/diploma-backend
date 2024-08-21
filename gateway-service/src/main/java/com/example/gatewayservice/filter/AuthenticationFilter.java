package com.example.gatewayservice.filter;

import com.example.gatewayservice.exception.MissingAuthorizationHeaderException;
import com.example.gatewayservice.utils.JwtUtil;
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

    private final JwtUtil jwtUtil;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                String authHeader = extractAuthHeader(exchange);

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new MissingAuthorizationHeaderException("Missing or invalid authorization header");
                }

                String token = authHeader.substring(7);
                jwtUtil.validateToken(token);
            }
            return chain.filter(exchange);
        };
    }

    private String extractAuthHeader(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    }

    public static class Config { }
}
