package com.e2rent.auth_service.utils;

import com.e2rent.auth_service.dto.AccessTokenResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.keycloak.OAuth2Constants;
import org.keycloak.representations.AccessTokenResponse;

public class TokenUtil {

    public static AccessTokenResponseDto processTokenResponse(AccessTokenResponse tokenResponse,
                                                              HttpServletResponse response, boolean isLogin) {
        if (tokenResponse.getRefreshToken() != null && isLogin) {
            setRefreshTokenCookie(tokenResponse.getRefreshToken(),
                    (int) tokenResponse.getRefreshExpiresIn(), response);
        }
        return new AccessTokenResponseDto(tokenResponse.getToken(), tokenResponse.getExpiresIn(),
                tokenResponse.getTokenType());
    }

    public static String extractRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (OAuth2Constants.REFRESH_TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private static void setRefreshTokenCookie(String refreshToken, int refreshExpiresInSeconds,
                                              HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie(OAuth2Constants.REFRESH_TOKEN, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // становити true, якщо використовуєте HTTPS
        refreshTokenCookie.setPath("/"); // Задайте коректний шлях
        refreshTokenCookie.setMaxAge(refreshExpiresInSeconds); // Використовуємо час життя refresh token
        response.addCookie(refreshTokenCookie);
    }

    public static void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie(OAuth2Constants.REFRESH_TOKEN, "");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // true, якщо HTTPS
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // видалення
        response.addCookie(refreshTokenCookie);
    }
}

