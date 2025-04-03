package com.e2rent.auth_service.utils;

import com.e2rent.auth_service.dto.AccessTokenResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.keycloak.OAuth2Constants;
import org.keycloak.representations.AccessTokenResponse;

public class TokenUtil {

    public static AccessTokenResponseDto processTokenResponse(AccessTokenResponse tokenResponse, HttpServletResponse response) {
        if (tokenResponse.getRefreshToken() != null) {
            setRefreshTokenCookie(tokenResponse.getRefreshToken(), response);
        }
        return new AccessTokenResponseDto(tokenResponse.getToken(), tokenResponse.getExpiresIn(),
                tokenResponse.getTokenType());
    }

    private static void setRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie(OAuth2Constants.REFRESH_TOKEN, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // Встановіть true, якщо використовуєте HTTPS
        refreshTokenCookie.setPath("/"); // Задайте коректний шлях
        refreshTokenCookie.setMaxAge(60 * 60 * 24); // 24 години
        response.addCookie(refreshTokenCookie);
    }
}

