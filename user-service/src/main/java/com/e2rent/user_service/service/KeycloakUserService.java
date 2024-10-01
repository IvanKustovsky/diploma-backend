package com.e2rent.user_service.service;

import com.e2rent.user_service.dto.RegisterUserDto;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakUserService {

    /**
     * Creates a new user in the Keycloak system based on the provided user details.
     *
     * @param registerUserDto the DTO containing the user information to be registered
     * @return true if the user was successfully created, false otherwise.
     */
    boolean createUser(RegisterUserDto registerUserDto);

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

