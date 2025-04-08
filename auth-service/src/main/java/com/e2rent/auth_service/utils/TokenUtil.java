package com.e2rent.auth_service.utils;

import com.e2rent.auth_service.dto.AccessTokenResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.keycloak.OAuth2Constants;
import org.keycloak.representations.AccessTokenResponse;

import java.time.Duration;

public class TokenUtil {

    private final static int REFRESH_COOKIE_AGE_DAYS = 1;
    private final static int REFRESH_COOKIE_AGE_HOURS = 12;
    private final static int REFRESH_COOKIE_AGE_MINUTES = 30;
    private static final int MAX_AGE_SECONDS = (int) Duration.ofDays(REFRESH_COOKIE_AGE_DAYS)
            .plusHours(REFRESH_COOKIE_AGE_HOURS)
            .plusMinutes(REFRESH_COOKIE_AGE_MINUTES)
            .toSeconds();

    public static AccessTokenResponseDto processTokenResponse(AccessTokenResponse tokenResponse,
                                                              HttpServletResponse response) {
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
        refreshTokenCookie.setMaxAge(MAX_AGE_SECONDS);
        response.addCookie(refreshTokenCookie);
    }
}

