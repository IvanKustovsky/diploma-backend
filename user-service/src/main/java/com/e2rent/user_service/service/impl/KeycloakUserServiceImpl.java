package com.e2rent.user_service.service.impl;

import com.e2rent.user_service.dto.RegisterUserDto;
import com.e2rent.user_service.service.KeycloakUserService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class KeycloakUserServiceImpl implements KeycloakUserService {

    private final RealmResource realmResource;

    @Override
    public boolean createUser(RegisterUserDto userDto) {
        UserRepresentation userRepresentation = buildUserRepresentation(userDto);
        UsersResource usersResource = getUsersResource();
        Response response = usersResource.create(userRepresentation);
        return isUserCreated(response);
    }

    private UserRepresentation buildUserRepresentation(RegisterUserDto registerUserDto) {
        var userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setEmail(registerUserDto.getEmail());
        userRepresentation.setUsername(registerUserDto.getEmail());
        userRepresentation.setFirstName(registerUserDto.getFirstName());
        userRepresentation.setLastName(registerUserDto.getLastName());

        var credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(registerUserDto.getPassword());
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        return userRepresentation;
    }

    private UsersResource getUsersResource() {
        return realmResource.users();
    }

    private boolean isUserCreated(Response response) {
        return Objects.equals(response.getStatus(), HttpStatus.CREATED.value());
    }

    @Override
    public UserRepresentation findByEmail(String email) {
        return getUsersResource().searchByEmail(email, true).getFirst();
    }

    @Override
    public void deleteById(String userId) {
        getUsersResource().delete(userId);
    }

    @Override
    public void deleteByEmail(String email) {
        deleteById(findByEmail(email).getId());
    }
}
