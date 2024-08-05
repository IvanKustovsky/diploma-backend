package com.example.user_service.audit;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@ComponentScan(basePackages = "com.example.user_service")
public class AuditConfig {
}
