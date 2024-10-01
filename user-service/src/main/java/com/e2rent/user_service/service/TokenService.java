package com.e2rent.user_service.service;

public interface TokenService {

    /**
     *
     * @param authorizationToken - authorization token
     * @return extracted email from the authorization token
     */
    String extractEmail(String authorizationToken);
}
