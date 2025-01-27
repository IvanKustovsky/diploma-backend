package com.e2rent.user_service.audit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuditAwareImplTest {

    @InjectMocks
    private AuditAwareImpl auditAwareImpl;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldReturnSystemWhenAuthenticationIsNullOrNotAuthenticated() {
        // given
        when(securityContext.getAuthentication()).thenReturn(null);

        // when
        Optional<String> auditor = auditAwareImpl.getCurrentAuditor();

        // then
        assertTrue(auditor.isPresent());
        assertEquals("System", auditor.get());

        // given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        // when
        auditor = auditAwareImpl.getCurrentAuditor();

        // then
        assertTrue(auditor.isPresent());
        assertEquals("System", auditor.get());
    }

    @Test
    void shouldReturnEmailWhenAuthenticationIsAuthenticatedAndJwtContainsEmail() {
        // given
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("email")).thenReturn("user@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(jwt);

        // when
        Optional<String> auditor = auditAwareImpl.getCurrentAuditor();

        // then
        assertTrue(auditor.isPresent());
        assertEquals("user@example.com", auditor.get());
    }

    @Test
    void shouldReturnSystemWhenAuthenticationIsAuthenticatedButJwtDoesNotContainEmail() {
        // given
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("email")).thenReturn(null);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(jwt);

        // when
        Optional<String> auditor = auditAwareImpl.getCurrentAuditor();

        // then
        assertTrue(auditor.isPresent());
        assertEquals("System", auditor.get());
    }

    @Test
    void shouldReturnSystemWhenPrincipalIsNotJwt() {
        // given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("NotJwtPrincipal");

        // when
        Optional<String> auditor = auditAwareImpl.getCurrentAuditor();

        // then
        assertTrue(auditor.isPresent());
        assertEquals("System", auditor.get());
    }
}