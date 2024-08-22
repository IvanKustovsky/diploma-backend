package com.example.identity_service.service;

import java.util.Collection;

public interface IJwtService {

    void validateToken(String token);

    String generateToken(String email, Collection<String> roles);
}
