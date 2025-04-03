package com.e2rent.auth_service.utils;

import com.e2rent.auth_service.config.KeycloakProperties;
import org.keycloak.OAuth2Constants;

import java.util.HashMap;
import java.util.Map;

public class TokenRequestUtil {

    public static Map<String, String> createTokenRequest(String grantType, String username,
                                                         String password, String refreshToken,
                                                         KeycloakProperties keycloakProperties) {
        Map<String, String> form = new HashMap<>();
        form.put(OAuth2Constants.GRANT_TYPE, grantType);
        form.put(OAuth2Constants.CLIENT_ID, keycloakProperties.getRestApiClientId());
        form.put(OAuth2Constants.CLIENT_SECRET, keycloakProperties.getRestApiClientSecret());

        if (username != null) form.put(OAuth2Constants.USERNAME, username);
        if (password != null) form.put(OAuth2Constants.PASSWORD, password);
        if (refreshToken != null) form.put(OAuth2Constants.REFRESH_TOKEN, refreshToken);

        return form;
    }
}
