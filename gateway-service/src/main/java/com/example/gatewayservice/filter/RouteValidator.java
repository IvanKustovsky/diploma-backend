package com.example.gatewayservice.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    // TODO: define all open api endpoints
    public static final List<String> openApiEndpoints = List.of(
            "/auth/api/v1/token",
            "/auth/api/v1/validate",
            "/equipments/api/v1/all",
            "/users/api/v1/register"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
