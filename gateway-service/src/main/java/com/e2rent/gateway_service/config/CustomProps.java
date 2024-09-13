package com.e2rent.gateway_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "open-api")
public class CustomProps {

    private List<String> endpoints;
}
