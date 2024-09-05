package com.example.identity_service.service.impl;

import com.example.identity_service.exception.MissingAuthorizationHeaderException;
import com.example.identity_service.service.IJwtService;
import com.example.identity_service.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements IJwtService {

    private final JwtUtil jwtUtil;

    @Override
    public boolean validateToken(final String token) {
        return jwtUtil.isTokenValid(token);
    }

    @Override
    public String generateToken(String email, Collection<String> roles) {
        return jwtUtil.generateToken(email, roles);
    }

    @Override
    public String extractEmail(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new MissingAuthorizationHeaderException("Missing or invalid authorization header");
        }
        String jwt = token.replace("Bearer ", "").trim();

        return jwtUtil.parseToken(jwt).getSubject();
    }
}
