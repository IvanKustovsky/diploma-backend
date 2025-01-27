package com.e2rent.user_service.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {


    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof Jwt jwt) {
                        String email = jwt.getClaim("email");
                        return Optional.ofNullable(email).orElse("System");
                    }
                    return "System"; // If principal isn't Jwt
                })
                .or(() -> Optional.of("System")); // If unauthenticated
    }
}
