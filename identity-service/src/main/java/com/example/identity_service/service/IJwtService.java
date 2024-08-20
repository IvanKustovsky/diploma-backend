package com.example.identity_service.service;

public interface IJwtService {

    void validateToken(String token);

    String generateToken(String email);
}
