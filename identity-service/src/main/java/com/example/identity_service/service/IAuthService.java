package com.example.identity_service.service;

import com.example.identity_service.model.AuthRequest;

public interface IAuthService {

    /**
     *
     * @param authRequest - AuthRequest Object
     * @return JWT token
     */
    String generateToken(AuthRequest authRequest);

    /**
     *
     * @param token - Token to validate
     */
    void validateToken(String token);

    /**
     *
     * @param token - JWT token
     * @return - extracted email from provided token
     */
    String extractEmail(String token);
}
