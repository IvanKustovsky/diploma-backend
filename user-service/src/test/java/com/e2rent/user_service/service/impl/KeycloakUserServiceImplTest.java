package com.e2rent.user_service.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.e2rent.user_service.dto.RegisterUserDto;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
public class KeycloakUserServiceImplTest {

    @Mock
    private RealmResource realmResource;

    @Mock
    private UsersResource usersResource;

    @Mock
    private Response response;

    @InjectMocks
    private KeycloakUserServiceImpl keycloakUserService;

    private RegisterUserDto validUserDto;

    @BeforeEach
    public void setUp() {
        // Initialize the valid user DTO with all required fields
        validUserDto = new RegisterUserDto();
        validUserDto.setEmail("test@example.com");
        validUserDto.setFirstName("Test");
        validUserDto.setLastName("User");
        validUserDto.setPassword("password123");
        validUserDto.setMobileNumber("+380973958378");

        // Mock the UserRepresentation
        mock(UserRepresentation.class);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // given
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(HttpStatus.CREATED.value());

        // when
        boolean isUserCreated = keycloakUserService.createUser(validUserDto);

        // then
        assertTrue(isUserCreated);
        verify(usersResource).create(any(UserRepresentation.class));
    }

    @Test
    void shouldReturnFalseWhenUserCreationFails() {
        // given
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(HttpStatus.BAD_REQUEST.value());

        // when
        boolean isUserCreated = keycloakUserService.createUser(validUserDto);

        // then
        assertFalse(isUserCreated);
        verify(usersResource).create(any(UserRepresentation.class));
    }

    @Test
    void shouldFindUserByEmail() {
        // given
        UserRepresentation mockUser = mock(UserRepresentation.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.searchByEmail("test@example.com", true))
                .thenReturn(Collections.singletonList(mockUser));

        // when
        UserRepresentation foundUser = keycloakUserService.findByEmail("test@example.com");

        // then
        assertNotNull(foundUser);
        assertEquals(mockUser, foundUser);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenUserNotFoundByEmail() {
        // given
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.searchByEmail("notfound@example.com", true))
                .thenReturn(Collections.emptyList());

        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            keycloakUserService.findByEmail("notfound@example.com");
        });
    }

    @Test
    void shouldDeleteUserById() {
        // given
        String userId = "user-id";
        Response mockedResponse = mock(Response.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.delete(userId)).thenReturn(mockedResponse);

        // when
        keycloakUserService.deleteById(userId);

        // then
        verify(usersResource).delete(userId);
    }

    @Test
    void shouldDeleteUserByEmail() {
        // given
        String email = "test@example.com";
        UserRepresentation mockUser = mock(UserRepresentation.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.searchByEmail(email, true))
                .thenReturn(Collections.singletonList(mockUser));
        when(mockUser.getId()).thenReturn("user-id");

        // when
        keycloakUserService.deleteByEmail(email);

        // then
        verify(usersResource).delete("user-id");
    }
}
