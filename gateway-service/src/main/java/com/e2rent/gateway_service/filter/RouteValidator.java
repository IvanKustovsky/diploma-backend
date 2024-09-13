package com.e2rent.gateway_service.filter;

import com.e2rent.gateway_service.config.CustomProps;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class RouteValidator {

    private final CustomProps customProps;

    public Predicate<ServerHttpRequest> isSecured;

    @PostConstruct
    public void init() {
        isSecured = request -> customProps.getEndpoints()
                .stream()
                .noneMatch(uri -> request.getURI().getPath().contains(uri));
    }
}
