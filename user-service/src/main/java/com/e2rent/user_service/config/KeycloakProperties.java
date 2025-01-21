package com.e2rent.user_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
public class KeycloakProperties {

    private String realm;
    private String domain;
    private String restApiClientId;
    private String restApiClientSecret;

    public String getAuthUrl() {
        return "http://" + domain;
    }
}
