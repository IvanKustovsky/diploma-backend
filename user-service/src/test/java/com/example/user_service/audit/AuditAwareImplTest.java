package com.example.user_service.audit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    void shouldReturnCurrentAuditor() {
        // given
        String expectedUsername = "user@gmail.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(expectedUsername);
        when(authentication.isAuthenticated()).thenReturn(true);

        // when
        Optional<String> auditor = auditAwareImpl.getCurrentAuditor();

        // then
        assertTrue(auditor.isPresent());
        assertEquals(expectedUsername, auditor.get());
    }

    @Test
    void shouldReturnEmptyWhenAuthenticationNameIsNull() {
        // given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(null);
        when(authentication.isAuthenticated()).thenReturn(true);

        // when
        Optional<String> auditor = auditAwareImpl.getCurrentAuditor();

        // then
        assertTrue(auditor.isEmpty());
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
}