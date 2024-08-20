package com.example.identity_service.service;

import com.example.identity_service.model.AuthRequest;

public interface IAuthService {

    String generateToken(AuthRequest authRequest);

    void validateToken(String token);
}
