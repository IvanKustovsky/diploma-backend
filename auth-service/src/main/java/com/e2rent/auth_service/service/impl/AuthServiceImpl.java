package com.e2rent.auth_service.service.impl;

import com.e2rent.auth_service.config.KeycloakProperties;
import com.e2rent.auth_service.dto.AccessTokenResponseDto;
import com.e2rent.auth_service.dto.LoginDto;
import com.e2rent.auth_service.exception.UserNotFoundException;
import com.e2rent.auth_service.service.IAuthService;
import com.e2rent.auth_service.dto.RegisterUserDto;
import com.e2rent.auth_service.service.client.KeycloakFeignClient;
import com.e2rent.auth_service.utils.TokenRequestUtil;
import com.e2rent.auth_service.utils.TokenUtil;
import com.e2rent.auth_service.utils.UserRepresentationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements IAuthService {

    private final KeycloakFeignClient keycloakFeignClient;
    private final KeycloakProperties keycloakProperties;

    @Override
    public boolean createUser(RegisterUserDto registerUserDto) {
        String adminAccessToken = getAdminAccessToken();
        var userRepresentation = UserRepresentationUtil.buildUserRepresentation(registerUserDto);

        var response = keycloakFeignClient.createUser(keycloakProperties.getRealm(),
                userRepresentation, "Bearer " + adminAccessToken);

        return response.getStatusCode() == HttpStatus.CREATED;
    }

    @Override
    public AccessTokenResponseDto getAccessToken(LoginDto loginDto, HttpServletResponse response) {
        var form = TokenRequestUtil.createTokenRequest(OAuth2Constants.PASSWORD, loginDto.getUsername(),
                loginDto.getPassword(), null, keycloakProperties);
        var tokenResponse = keycloakFeignClient.getToken(keycloakProperties.getRealm(), form);
        return TokenUtil.processTokenResponse(tokenResponse, response, true);
    }

    @Override
    public AccessTokenResponseDto refreshToken(String refreshToken, HttpServletResponse response) {
        var form = TokenRequestUtil.createTokenRequest(OAuth2Constants.REFRESH_TOKEN,
                null, null,
                refreshToken, keycloakProperties);
        var tokenResponse = keycloakFeignClient.refreshToken(keycloakProperties.getRealm(), form);
        return TokenUtil.processTokenResponse(tokenResponse, response, false);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = TokenUtil.extractRefreshTokenFromCookies(request);

        if (refreshToken != null) {
            performKeycloakLogout(refreshToken);
        }

        TokenUtil.clearRefreshTokenCookie(response);
    }

    @Override
    public UserRepresentation findByEmail(String email) {
        String adminAccessToken = getAdminAccessToken();
        var usersRepresentation = keycloakFeignClient.findUsersByEmail(
                keycloakProperties.getRealm(), email, true, "Bearer " + adminAccessToken);

        if (usersRepresentation == null || usersRepresentation.isEmpty()) {
            throw new UserNotFoundException(email);
        }

        return usersRepresentation.getFirst();
    }

    @Override
    public void deleteById(String userId) {
        String adminAccessToken = getAdminAccessToken();
        keycloakFeignClient.deleteUser(keycloakProperties.getRealm(), userId,
                "Bearer " + adminAccessToken);
    }

    @Override
    public void deleteByEmail(String email) {
        deleteById(findByEmail(email).getId());
    }

    private String getAdminAccessToken() {
        Map<String, String> form = Map.of(
                OAuth2Constants.GRANT_TYPE, OAuth2Constants.CLIENT_CREDENTIALS,
                OAuth2Constants.CLIENT_ID, keycloakProperties.getRestApiClientId(),
                OAuth2Constants.CLIENT_SECRET, keycloakProperties.getRestApiClientSecret()
        );

        return keycloakFeignClient.getToken(keycloakProperties.getRealm(), form).getToken();
    }

    private void performKeycloakLogout(String refreshToken) {
        var form = TokenRequestUtil.createTokenRequest(OAuth2Constants.REFRESH_TOKEN,
                null, null,
                refreshToken, keycloakProperties);

        var logoutResponse = keycloakFeignClient.logout(keycloakProperties.getRealm(), form);

        if (logoutResponse.getStatusCode() != HttpStatus.NO_CONTENT) {
            throw new IllegalStateException("Unexpected Keycloak logout response: " + logoutResponse.getStatusCode());
        }

        log.info("Successfully logged out from Keycloak.");
    }
}
