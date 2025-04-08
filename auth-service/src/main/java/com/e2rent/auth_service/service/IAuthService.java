package com.e2rent.auth_service.service;

import com.e2rent.auth_service.dto.AccessTokenResponseDto;
import com.e2rent.auth_service.dto.LoginDto;
import com.e2rent.auth_service.dto.RegisterUserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.keycloak.representations.idm.UserRepresentation;

public interface IAuthService {

    /**
     * Creates a new user in the Keycloak system based on the provided user details.
     *
     * @param registerUserDto the DTO containing the user information to be registered.
     * @return true if the user was successfully created, false otherwise.
     */
    boolean createUser(RegisterUserDto registerUserDto);

    /**
     * Retrieves an access token from Keycloak for a user based on their login credentials.
     *
     * @param loginDto the DTO containing the user's login information (username and password).
     * @param response the HttpServletResponse used to set authentication-related cookies if needed.
     * @return an AccessTokenResponseDto containing the access token, its expiration time, and token type.
     */
    AccessTokenResponseDto getAccessToken(LoginDto loginDto, HttpServletResponse response);

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param refreshToken the refresh token provided during initial authentication.
     * @param response the HttpServletResponse used to set updated authentication-related cookies if needed.
     * @return a new AccessTokenResponseDto containing a refreshed access token and its expiration details.
     */
    AccessTokenResponseDto refreshToken(String refreshToken, HttpServletResponse response);

    /**
     * Logs out the currently authenticated user by removing the refresh token cookie.
     * This effectively ends the user's session on the client side.
     *
     * @param response the HttpServletResponse used to remove authentication-related cookies (e.g., refresh token).
     */
    void logout(HttpServletResponse response);

    /**
     * Retrieves a user representation from Keycloak by their email address.
     *
     * @param email the email address of the user to be found.
     * @return a UserRepresentation object containing the user's information or null if not found.
     */
    UserRepresentation findByEmail(String email);

    /**
     * Deletes a user from Keycloak by their unique user ID.
     *
     * @param userId the unique identifier of the user to be deleted.
     */
    void deleteById(String userId);

    /**
     * Deletes a user from Keycloak by their email address.
     *
     * @param email the email address of the user to be deleted.
     */
    void deleteByEmail(String email);
}
