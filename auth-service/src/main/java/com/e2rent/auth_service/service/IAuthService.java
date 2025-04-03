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
     * @param registerUserDto the DTO containing the user information to be registered
     * @return true if the user was successfully created, false otherwise.
     */
    boolean createUser(RegisterUserDto registerUserDto);

    AccessTokenResponseDto getAccessToken(LoginDto loginDto, HttpServletResponse response);

    AccessTokenResponseDto refreshToken(String refreshToken, HttpServletResponse response);

    /**
     * Retrieves a user representation from Keycloak by their email address.
     *
     * @param email the email address of the user to be found.
     * @return a UserRepresentation object containing the user's information.
     */
    UserRepresentation findByEmail(String email);

    /**
     * Deletes a user from Keycloak by their user ID.
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
