package com.e2rent.auth_service.utils;

import com.e2rent.auth_service.dto.RegisterUserDto;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;

public class UserRepresentationUtil {

    public static UserRepresentation buildUserRepresentation(RegisterUserDto registerUserDto) {
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
}

