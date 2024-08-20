package com.example.identity_service.service.impl;

import com.example.identity_service.model.AuthRequest;
import com.example.identity_service.service.IAuthService;
import com.example.identity_service.service.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String generateToken(AuthRequest authRequest) {
        var emailPassAuthToken = new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                authRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(emailPassAuthToken);
        if (authenticate.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @Override
    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
