package com.example.identity_service.service.impl;

import com.example.identity_service.model.AuthRequest;
import com.example.identity_service.service.IAuthService;
import com.example.identity_service.service.IJwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String generateToken(AuthRequest authRequest) {
        var emailPassAuthToken = new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                authRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(emailPassAuthToken);

        Collection<String> roles = mapToRoles(authentication.getAuthorities());
        return jwtService.generateToken(authRequest.getEmail(), roles);
    }

    @Override
    public void validateToken(String token) {
        if(!jwtService.validateToken(token)) {
            throw new JwtException("Invalid JWT token");
        }
    }

    @Override
    public String extractEmail(String token) {
        return jwtService.extractEmail(token);
    }

    private Collection<String> mapToRoles(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
