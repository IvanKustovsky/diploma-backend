package com.example.user_service.service.impl;

import com.example.user_service.constants.RoleConstants;
import com.example.user_service.dto.CompanyDto;
import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.Company;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.UserEntity;
import com.example.user_service.exception.ResourceNotFoundException;
import com.example.user_service.exception.UserAlreadyExistsException;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.ICompanyService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("User Service Test Class")
class UserEntityServiceImplTest {

    private static final Role USER_ROLE = Role.builder()
            .name(RoleConstants.USER_ROLE).build();
    private static final List<Role> USER_ROLES = new ArrayList<>(Collections.singletonList(USER_ROLE));
    private static final String CURRENT_EMAIL = "current@example.com";
    private static final String NEW_EMAIL = "new@example.com";
    private static final String FULL_NAME = "Updated Name";
    private static final String MOBILE_NUMBER = "987654321";
    private static final String NEW_PASSWORD = "newpassword";
    private static final String OLD_NAME = "Old Name";
    private static final String OLD_MOBILE_NUMBER = "123456789";
    private static final String OLD_PASSWORD = "oldpassword";

    private static final UserEntity EXISTING_USER = UserEntity.builder()
            .email(CURRENT_EMAIL)
            .fullName(OLD_NAME)
            .mobileNumber(OLD_MOBILE_NUMBER)
            .password(OLD_PASSWORD)
            .roles(USER_ROLES)
            .build();

    private static final UserDto USER_DTO = UserDto.builder()
            .email(CURRENT_EMAIL)
            .fullName(OLD_NAME)
            .mobileNumber(OLD_MOBILE_NUMBER)
            .password(OLD_PASSWORD)
            .roles(USER_ROLES)
            .build();

    private static final UserDto USER_UPDATE_DTO = UserDto.builder()
            .email(NEW_EMAIL)
            .fullName(FULL_NAME)
            .mobileNumber(MOBILE_NUMBER)
            .password(NEW_PASSWORD)
            .roles(USER_ROLES)
            .build();

    private static final UserEntity UPDATED_USER = UserEntity.builder()
            .email(NEW_EMAIL)
            .fullName(FULL_NAME)
            .mobileNumber(MOBILE_NUMBER)
            .password(NEW_PASSWORD)
            .roles(USER_ROLES)
            .build();

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private RoleRepository roleRepositoryMock;

    @Mock
    private ICompanyService companyServiceMock;

    @Mock
    private AuditorAware<String> auditAwareMock;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("Should throw ResourceNotFoundException when fetching non-existent user")
    @Order(1)
    void fetchUserWhenNotExists() {
        // given
        when(userRepositoryMock.findByEmail(USER_DTO.getEmail())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> userServiceImpl.fetchUser(USER_DTO.getEmail()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "User", "email", USER_DTO.getEmail()));

        verify(userRepositoryMock, times(1)).findByEmail(USER_DTO.getEmail());
    }

    @Test
    @DisplayName("Should fetch user when it exists")
    @Order(2)
    void fetchUserWhenExists() {
        // given
        when(userRepositoryMock.findByEmail(USER_DTO.getEmail())).thenReturn(Optional.of(EXISTING_USER));

        // when
        UserDto result = userServiceImpl.fetchUser(USER_DTO.getEmail());

        // then
        assertEquals(USER_DTO, result);
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when trying to register user with existing email")
    @Order(3)
    void registerUserWhenEmailExists() {
        // given
        when(userRepositoryMock.findByEmail(USER_DTO.getEmail())).thenReturn(Optional.of(EXISTING_USER));

        // when, then
        assertThatThrownBy(() -> userServiceImpl.registerUser(USER_DTO))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User already registered with provided email or mobile number");

        verify(userRepositoryMock, times(1)).findByEmail(USER_DTO.getEmail());
        verify(userRepositoryMock, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should register user without company when email does not exist")
    @Order(4)
    void registerUserWithoutCompanyWhenEmailDoesNotExist() {
        // given
        when(userRepositoryMock.findByEmail(USER_DTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoderMock.encode(USER_DTO.getPassword())).thenReturn("encodedPassword");
        when(roleRepositoryMock.getRoleByName(RoleConstants.USER_ROLE))
                .thenReturn(Optional.of(USER_ROLE));
        when(userRepositoryMock.save(any(UserEntity.class))).thenReturn(UPDATED_USER);

        // when
        userServiceImpl.registerUser(USER_DTO);

        // then
        verify(userRepositoryMock, times(1)).findByEmail(USER_DTO.getEmail());
        verify(userRepositoryMock, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should register user with company when email does not exist")
    @Order(5)
    void registerUserWithCompanyWhenEmailDoesNotExist() {
        // given
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode("11665522");
        companyDto.setName("Company Name");
        companyDto.setAddress("Company Address");
        USER_DTO.setCompany(companyDto);

        when(userRepositoryMock.findByEmail(USER_DTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoderMock.encode(USER_DTO.getPassword())).thenReturn("encodedPassword");
        when(roleRepositoryMock.getRoleByName(RoleConstants.USER_ROLE))
                .thenReturn(Optional.of(USER_ROLE));
        when(userRepositoryMock.save(any(UserEntity.class))).thenReturn(UPDATED_USER);
        when(companyServiceMock.registerCompany(companyDto)).thenReturn(new Company());

        // when
        userServiceImpl.registerUser(USER_DTO);

        // then
        verify(userRepositoryMock, times(1)).findByEmail(USER_DTO.getEmail());
        verify(userRepositoryMock, times(1)).save(any(UserEntity.class));

        ArgumentCaptor<UserEntity> userArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepositoryMock, times(1)).save(userArgumentCaptor.capture());

        UserEntity savedUser = userArgumentCaptor.getValue();
        assertEquals(USER_DTO.getEmail(), savedUser.getEmail());
        assertEquals(USER_DTO.getFullName(), savedUser.getFullName());
        assertEquals(USER_DTO.getMobileNumber(), savedUser.getMobileNumber());
        assertNotNull(savedUser.getCompany());
        assertEquals(USER_ROLE, savedUser.getRoles().getFirst());
    }

    @Test
    @DisplayName("Should delete user and clear roles")
    @Transactional
    @Order(6)
    void deleteUser() {
        // given
        when(userRepositoryMock.findByEmail(USER_DTO.getEmail())).thenReturn(Optional.of(EXISTING_USER));

        // when
        userServiceImpl.deleteUser(USER_DTO.getEmail());

        // then
        verify(userRepositoryMock, times(1)).findByEmail(USER_DTO.getEmail());
        verify(userRepositoryMock, times(1)).save(any(UserEntity.class));
        verify(userRepositoryMock, times(1)).deleteById(EXISTING_USER.getId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent user")
    @Transactional
    @Order(7)
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
    @DisplayName("Should successfully update user without changing email or mobile number")
    @Order(8)
    void updateUserSuccessfullyWithoutChangingEmailOrMobileNumber() {
        // given
        UserDto userDto = UserDto.builder()
                .email(CURRENT_EMAIL) // unchanged email
                .fullName(FULL_NAME)
                .mobileNumber(OLD_MOBILE_NUMBER) // unchanged mobile number
                .password(NEW_PASSWORD)
                .roles(USER_ROLES)
                .build();

        when(userRepositoryMock.findByEmail(CURRENT_EMAIL)).thenReturn(Optional.of(EXISTING_USER));
        when(passwordEncoderMock.encode(NEW_PASSWORD)).thenReturn("encodedPassword");
        when(auditAwareMock.getCurrentAuditor()).thenReturn(Optional.of(CURRENT_EMAIL));

        // Capture the saved user
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        when(userRepositoryMock.save(userCaptor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        boolean result = userServiceImpl.updateUser(userDto);

        // then
        assertTrue(result);
        verify(userRepositoryMock, times(1)).save(any(UserEntity.class));
        UserEntity updatedUser = userCaptor.getValue();
        assertEquals(CURRENT_EMAIL, updatedUser.getEmail());
        assertEquals(FULL_NAME, updatedUser.getFullName());
        assertEquals(OLD_MOBILE_NUMBER, updatedUser.getMobileNumber());
        assertEquals("encodedPassword", updatedUser.getPassword());
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when new email exists")
    @Order(9)
    void updateUserWithExistingEmail() {
        // given
        when(userRepositoryMock.findByEmail(CURRENT_EMAIL)).thenReturn(Optional.of(EXISTING_USER));
        when(userRepositoryMock.findByEmail(NEW_EMAIL)).thenReturn(Optional.of(new UserEntity()));
        when(auditAwareMock.getCurrentAuditor()).thenReturn(Optional.of(CURRENT_EMAIL));

        // when, then
        assertThatThrownBy(() -> userServiceImpl.updateUser(USER_UPDATE_DTO))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already registered with provided email or mobile number");

        verify(userRepositoryMock, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when new mobile number exists")
    @Order(10)
    void updateUserWithExistingMobileNumber() {
        // given
        when(userRepositoryMock.findByEmail(CURRENT_EMAIL)).thenReturn(Optional.of(EXISTING_USER));
        when(userRepositoryMock.findByEmail(NEW_EMAIL)).thenReturn(Optional.empty());
        when(userRepositoryMock.findByMobileNumber(MOBILE_NUMBER)).thenReturn(Optional.of(new UserEntity()));
        when(auditAwareMock.getCurrentAuditor()).thenReturn(Optional.of(CURRENT_EMAIL));

        // when, then
        assertThatThrownBy(() -> userServiceImpl.updateUser(USER_UPDATE_DTO))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already registered with provided email or mobile number");

        verify(userRepositoryMock, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when both new email and new mobile number exist")
    @Order(11)
    void updateUserWithExistingEmailAndMobileNumber() {
        // given
        when(userRepositoryMock.findByEmail(CURRENT_EMAIL)).thenReturn(Optional.of(EXISTING_USER));
        when(userRepositoryMock.findByEmail(NEW_EMAIL)).thenReturn(Optional.of(new UserEntity()));
        when(userRepositoryMock.findByMobileNumber(MOBILE_NUMBER)).thenReturn(Optional.of(new UserEntity()));
        when(auditAwareMock.getCurrentAuditor()).thenReturn(Optional.of(CURRENT_EMAIL));

        // when, then
        assertThatThrownBy(() -> userServiceImpl.updateUser(USER_UPDATE_DTO))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already registered with provided email or mobile number");

        verify(userRepositoryMock, never()).save(any(UserEntity.class));
    }

}