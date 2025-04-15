package com.e2rent.user_service.service.impl;

import com.e2rent.user_service.dto.CompanyDto;
import com.e2rent.user_service.dto.RegisterUserDto;
import com.e2rent.user_service.dto.UpdateUserDto;
import com.e2rent.user_service.dto.UserDto;
import com.e2rent.user_service.entity.Company;
import com.e2rent.user_service.entity.UserEntity;
import com.e2rent.user_service.exception.ResourceNotFoundException;
import com.e2rent.user_service.exception.UserAlreadyExistsException;
import com.e2rent.user_service.repository.UserRepository;
import com.e2rent.user_service.service.ICompanyService;
import com.e2rent.user_service.service.TokenService;
import com.e2rent.user_service.service.client.AuthFeignClient;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("User Service Test Class")
class UserEntityServiceImplTest {

    private static final String CURRENT_EMAIL = "current@example.com";
    private static final String MOBILE_NUMBER = "987654321";
    private static final String OLD_FIRST_NAME = "OldFirstName";
    private static final String OLD_LAST_NAME = "OldLastName";
    private static final String NEW_FIRST_NAME = "NewFirstName";
    private static final String NEW_LAST_NAME = "NewLastName";
    private static final String OLD_MOBILE_NUMBER = "123456789";
    private static final String OLD_PASSWORD = "old_password";
    private static final String AUTHORIZATION_TOKEN = "Bearer some.jwt.token";

    private static final UserEntity EXISTING_USER = UserEntity.builder()
            .email(CURRENT_EMAIL)
            .fullName(OLD_FIRST_NAME + " " + OLD_LAST_NAME)
            .mobileNumber(OLD_MOBILE_NUMBER)
            .build();

    private static final RegisterUserDto REGISTER_USER_DTO = RegisterUserDto.builder()
            .email(CURRENT_EMAIL)
            .firstName(OLD_FIRST_NAME)
            .lastName(OLD_LAST_NAME)
            .mobileNumber(OLD_MOBILE_NUMBER)
            .password(OLD_PASSWORD)
            .build();

    private static final UserDto USER_DTO = UserDto.builder()
            .email(CURRENT_EMAIL)
            .fullName(OLD_FIRST_NAME + " " + OLD_LAST_NAME)
            .mobileNumber(OLD_MOBILE_NUMBER)
            .build();

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private ICompanyService companyServiceMock;

    @Mock
    private AuthFeignClient authFeignClient;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("Should throw ResourceNotFoundException when fetching non-existent user")
    @Order(1)
    void fetchUserByEmailWhenNotExists() {
        // given
        when(userRepositoryMock.findByEmail(USER_DTO.getEmail())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> userServiceImpl.fetchUserByEmail(USER_DTO.getEmail()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "User", "email", USER_DTO.getEmail()));

        verify(userRepositoryMock, times(1)).findByEmail(USER_DTO.getEmail());
    }

    @Test
    @DisplayName("Should fetch user when it exists")
    @Order(2)
    void fetchUserByEmailWhenExists() {
        // given
        when(userRepositoryMock.findByEmail(USER_DTO.getEmail())).thenReturn(Optional.of(EXISTING_USER));

        // when
        UserDto result = userServiceImpl.fetchUserByEmail(USER_DTO.getEmail());

        // then
        assertEquals(USER_DTO, result);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when fetching non-existent user")
    @Order(3)
    void fetchUserByIdWhenNotExists() {
        // given
        when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> userServiceImpl.fetchUserById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "User", "ID", 1L));

        verify(userRepositoryMock, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should fetch user when it exists")
    @Order(4)
    void fetchUserByIdWhenExists() {
        // given
        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(EXISTING_USER));

        // when
        UserDto result = userServiceImpl.fetchUserById(1L);

        // then
        assertEquals(USER_DTO, result);
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when trying to register user with existing email")
    @Order(5)
    void registerUserWhenEmailExists() {
        // given
        when(userRepositoryMock.findByEmail(REGISTER_USER_DTO.getEmail()))
                .thenReturn(Optional.of(EXISTING_USER));

        // when, then
        assertThatThrownBy(() -> userServiceImpl.registerUser(REGISTER_USER_DTO))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User already registered with provided email");

        verify(userRepositoryMock, times(1)).findByEmail(REGISTER_USER_DTO.getEmail());
        verify(userRepositoryMock, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should register user without company when email does not exist")
    @Order(6)
    void registerUserWithoutCompanyWhenEmailDoesNotExist() {
        // given
        var responseEntity = ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        when(userRepositoryMock.findByEmail(REGISTER_USER_DTO.getEmail()))
                .thenReturn(Optional.empty());
        when(authFeignClient.registerUser(REGISTER_USER_DTO)).thenReturn(responseEntity);
        when(userRepositoryMock.save(any(UserEntity.class))).thenReturn(any(UserEntity.class));

        // when
        userServiceImpl.registerUser(REGISTER_USER_DTO);

        // then
        verify(userRepositoryMock, times(1)).findByEmail(REGISTER_USER_DTO.getEmail());
        verify(userRepositoryMock, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should register user with company when email does not exist")
    @Order(7)
    void registerUserWithCompanyWhenEmailDoesNotExist() {
        // given
        var responseEntity = ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode("11665522");
        companyDto.setName("Company Name");
        companyDto.setAddress("Company Address");
        REGISTER_USER_DTO.setCompany(companyDto);

        when(userRepositoryMock.findByEmail(REGISTER_USER_DTO.getEmail())).thenReturn(Optional.empty());
        when(userRepositoryMock.save(any(UserEntity.class))).thenReturn(new UserEntity());
        when(companyServiceMock.registerCompany(companyDto)).thenReturn(new Company());
        when(authFeignClient.registerUser(REGISTER_USER_DTO)).thenReturn(responseEntity);

        // when
        userServiceImpl.registerUser(REGISTER_USER_DTO);

        // then
        verify(userRepositoryMock, times(1)).findByEmail(REGISTER_USER_DTO.getEmail());
        verify(userRepositoryMock, times(1)).save(any(UserEntity.class));

        ArgumentCaptor<UserEntity> userArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepositoryMock, times(1)).save(userArgumentCaptor.capture());

        UserEntity savedUser = userArgumentCaptor.getValue();
        assertEquals(REGISTER_USER_DTO.getEmail(), savedUser.getEmail());
        assertEquals(REGISTER_USER_DTO.getMobileNumber(), savedUser.getMobileNumber());
        assertNotNull(savedUser.getCompany());
    }

    @Test
    @DisplayName("Should delete user and clear roles")
    @Transactional
    @Order(8)
    void deleteUser() {
        // given
        when(userRepositoryMock.findByEmail(USER_DTO.getEmail())).thenReturn(Optional.of(EXISTING_USER));

        // when
        userServiceImpl.deleteUser(USER_DTO.getEmail());

        // then
        verify(userRepositoryMock, times(1)).findByEmail(USER_DTO.getEmail());
        verify(userRepositoryMock, times(1)).deleteById(EXISTING_USER.getId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent user")
    @Transactional
    @Order(9)
    void deleteUserWhenNotExists() {
        // given
        when(userRepositoryMock.findByEmail(USER_DTO.getEmail())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> userServiceImpl.deleteUser(USER_DTO.getEmail()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "User", "email", USER_DTO.getEmail()));

        verify(userRepositoryMock, times(1)).findByEmail(USER_DTO.getEmail());
        verify(userRepositoryMock, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should successfully update user without changing mobile number")
    @Order(10)
    void updateUserSuccessfullyWithoutChangingMobileNumber() {
        // given
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .mobileNumber(EXISTING_USER.getMobileNumber())
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .build();

        when(tokenService.extractEmail(AUTHORIZATION_TOKEN)).thenReturn(CURRENT_EMAIL);
        when(userRepositoryMock.findByEmail(CURRENT_EMAIL)).thenReturn(Optional.of(EXISTING_USER));

        // when
        boolean result = userServiceImpl.updateUser(updateUserDto, AUTHORIZATION_TOKEN);

        // then
        assertTrue(result);
        verify(userRepositoryMock, times(1)).findByEmail(CURRENT_EMAIL);
        verify(tokenService, times(1)).extractEmail(AUTHORIZATION_TOKEN);

        assertEquals(NEW_FIRST_NAME + " " + NEW_LAST_NAME, EXISTING_USER.getFullName());
        assertEquals(OLD_MOBILE_NUMBER, EXISTING_USER.getMobileNumber());
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when new mobile number exists")
    @Order(11)
    void updateUserWithExistingMobileNumber() {
        // given
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .mobileNumber(MOBILE_NUMBER)
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .build();

        when(tokenService.extractEmail(AUTHORIZATION_TOKEN)).thenReturn(CURRENT_EMAIL);
        when(userRepositoryMock.findByEmail(CURRENT_EMAIL)).thenReturn(Optional.of(EXISTING_USER));
        when(userRepositoryMock.findByMobileNumber(MOBILE_NUMBER)).thenReturn(Optional.of(new UserEntity()));

        // when, then
        assertThatThrownBy(() -> userServiceImpl.updateUser(updateUserDto, AUTHORIZATION_TOKEN))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already registered with provided mobile number");

        verify(userRepositoryMock, never()).save(any(UserEntity.class));
    }
}