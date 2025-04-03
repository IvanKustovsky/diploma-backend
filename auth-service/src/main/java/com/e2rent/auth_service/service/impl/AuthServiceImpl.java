package com.e2rent.auth_service.service.impl;

import com.e2rent.auth_service.config.KeycloakProperties;
import com.e2rent.auth_service.dto.AccessTokenResponseDto;
import com.e2rent.auth_service.dto.LoginDto;
import com.e2rent.auth_service.service.IAuthService;
import com.e2rent.auth_service.dto.RegisterUserDto;
import com.e2rent.auth_service.service.client.KeycloakFeignClient;
import com.e2rent.auth_service.utils.TokenRequestUtil;
import com.e2rent.auth_service.utils.TokenUtil;
import com.e2rent.auth_service.utils.UserRepresentationUtil;
import feign.FeignException;
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

    private String getAdminAccessToken() {
        Map<String, String> form = Map.of(
                OAuth2Constants.GRANT_TYPE, OAuth2Constants.CLIENT_CREDENTIALS,
                OAuth2Constants.CLIENT_ID, keycloakProperties.getRestApiClientId(),
                OAuth2Constants.CLIENT_SECRET, keycloakProperties.getRestApiClientSecret()
        );

        return keycloakFeignClient.getToken(keycloakProperties.getRealm(), form).getToken();
    }

    @Override
    public boolean createUser(RegisterUserDto registerUserDto) {
        String adminAccessToken = getAdminAccessToken();
        var userRepresentation = UserRepresentationUtil.buildUserRepresentation(registerUserDto);
        try {
            var response = keycloakFeignClient.createUser(keycloakProperties.getRealm(),
                    userRepresentation, "Bearer " + adminAccessToken);
            log.info("Response status: {}", response.getStatusCode());
            return response.getStatusCode() == HttpStatus.CREATED; // 201
        } catch (FeignException e) { // TODO Process in GlobalExceptionHandler
            log.error("Failed to create user: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public AccessTokenResponseDto getAccessToken(LoginDto loginDto, HttpServletResponse response) {
        var form = TokenRequestUtil.createTokenRequest(OAuth2Constants.PASSWORD, loginDto.getUsername(),
                loginDto.getPassword(), null, keycloakProperties);
        var tokenResponse = keycloakFeignClient.getToken(keycloakProperties.getRealm(), form);
        return TokenUtil.processTokenResponse(tokenResponse, response);
    }

    @Override
    public AccessTokenResponseDto refreshToken(String refreshToken, HttpServletResponse response) {
        var form = TokenRequestUtil.createTokenRequest(OAuth2Constants.REFRESH_TOKEN, null, null,
                refreshToken, keycloakProperties);
        var tokenResponse = keycloakFeignClient.refreshToken(keycloakProperties.getRealm(), form);
        return TokenUtil.processTokenResponse(tokenResponse, response);
    }

    @Override
    public UserRepresentation findByEmail(String email) {
        String adminAccessToken = getAdminAccessToken(); // Отримуємо admin-токен
        return keycloakFeignClient.findUsersByEmail(
                keycloakProperties.getRealm(),
                email,
                true,
                "Bearer " + adminAccessToken // Додаємо токен у заголовок
        ).getFirst();
    }

    @Override
    public void deleteById(String userId) {
        String adminAccessToken = getAdminAccessToken(); // Отримуємо admin-токен
        keycloakFeignClient.deleteUser(keycloakProperties.getRealm(), userId,
                "Bearer " + adminAccessToken);
    }

    @Override
    public void deleteByEmail(String email) {
        deleteById(findByEmail(email).getId());
    }
}
