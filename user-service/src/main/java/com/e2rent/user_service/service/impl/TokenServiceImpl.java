package com.e2rent.user_service.service.impl;

import com.e2rent.user_service.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtDecoder jwtDecoder;

    @Override
    public String extractEmail(String authorizationToken) {
        String tokenValue = authorizationToken.substring(7);
        var jwt = jwtDecoder.decode(tokenValue);
        return jwt.getClaim("email");
    }
}
