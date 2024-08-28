package com.example.identity_service.service;

import java.util.Collection;

public interface IJwtService {

    /**
     *
     * @param token - JWT token to validate
     */
    void validateToken(String token);

    /**
     *
     * @param email - User email
     * @param roles - User roles
     * @return - generated JWT token based on email and roles
     */
    String generateToken(String email, Collection<String> roles);

    /**
     *
     * @param token - Authorization token
     * @return - Extracted email from token
     */
    String extractEmail(String token);
}
