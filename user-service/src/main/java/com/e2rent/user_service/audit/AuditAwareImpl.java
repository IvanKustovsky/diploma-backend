package com.e2rent.user_service.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {
    /**
     * @return the current auditor
     */
    // TODO: probably fix it and replace tests
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("System");
    }
}
