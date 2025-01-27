package com.e2rent.user_service.service.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Token Service Test Class")
public class TokenServiceImplTest {

    @Mock
    private JwtDecoder jwtDecoder;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private static final String VALID_TOKEN = "Bearer valid.token.here";
    private static final String INVALID_TOKEN = "Bearer invalid.token";
    private static final String EMAIL = "test@example.com";

    @Test
    void shouldReturnEmailWhenTokenIsValid() {
        // given
        Jwt jwt = mock(Jwt.class);
        when(jwtDecoder.decode("valid.token.here")).thenReturn(jwt);
        when(jwt.getClaim("email")).thenReturn(EMAIL);

        // when
        String email = tokenService.extractEmail(VALID_TOKEN);

        // then
        assertEquals(EMAIL, email);
    }

    @Test
    void shouldReturnNullWhenEmailClaimIsNotPresent() {
        // given
        Jwt jwt = mock(Jwt.class);
        when(jwtDecoder.decode("valid.token.here")).thenReturn(jwt);
        when(jwt.getClaim("email")).thenReturn(null);

        // when
        String email = tokenService.extractEmail(VALID_TOKEN);

        // then
        assertNull(email);
    }

    @Test
    void shouldThrowExceptionWhenTokenIsInvalid() {
        // given
        when(jwtDecoder.decode("invalid.token")).thenThrow(new JwtException("Invalid token"));

        // when & then
        assertThrows(JwtException.class, () -> tokenService.extractEmail(INVALID_TOKEN));
    }
}
