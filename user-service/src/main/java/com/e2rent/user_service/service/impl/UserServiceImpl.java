package com.e2rent.user_service.service.impl;

import com.e2rent.user_service.dto.RegisterUserDto;
import com.e2rent.user_service.dto.UpdateUserDto;
import com.e2rent.user_service.dto.UserDto;
import com.e2rent.user_service.entity.Company;
import com.e2rent.user_service.exception.ResourceNotFoundException;
import com.e2rent.user_service.service.IUserService;
import com.e2rent.user_service.entity.UserEntity;
import com.e2rent.user_service.exception.UserAlreadyExistsException;
import com.e2rent.user_service.mapper.UserMapper;
import com.e2rent.user_service.repository.UserRepository;
import com.e2rent.user_service.service.ICompanyService;
import com.e2rent.user_service.service.KeycloakUserService;
import com.e2rent.user_service.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final ICompanyService companyService;
    private final KeycloakUserService keycloakUserService;
    private final TokenService tokenService;

    @Override
    @Transactional
    public void registerUser(RegisterUserDto registerUserDto) {
        checkIfUserExists(registerUserDto.getEmail(), registerUserDto.getMobileNumber());

        var companyDto = registerUserDto.getCompany();
        Company company = null;
        if (companyDto != null) {
            company = companyService.registerCompany(companyDto);
        }

        UserEntity user = UserMapper.INSTANCE.toEntity(registerUserDto);
        user.setCompany(company);

        keycloakUserService.createUser(registerUserDto);

        userRepository.save(user);
    }

    @Override
    public UserDto fetchUser(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    @Transactional
    public boolean updateUser(UpdateUserDto updateUserDto, String authorizationToken) {
        String currentUserEmail = tokenService.extractEmail(authorizationToken);

        UserEntity existingUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUserEmail));

        if (!updateUserDto.getMobileNumber().equals(existingUser.getMobileNumber())) {
            checkIfUserExistsByMobileNumber(updateUserDto.getMobileNumber());
        }

        UserMapper.INSTANCE.updateUserEntityFromDto(updateUserDto, existingUser);

        return true;
    }

    @Override
    @Transactional
    public boolean deleteUser(String email) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        userRepository.deleteById(existingUser.getId());
        keycloakUserService.deleteByEmail(email);
        return true;
    }

    private void checkIfUserExists(String email, String mobileNumber) {
        checkIfUserExistsByEmail(email);
        checkIfUserExistsByMobileNumber(mobileNumber);
    }

    private void checkIfUserExistsByEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User already registered with provided email");
                });
    }

    private void checkIfUserExistsByMobileNumber(String mobileNumber) {
        userRepository.findByMobileNumber(mobileNumber)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User already registered with provided mobile number");
                });
    }
}
