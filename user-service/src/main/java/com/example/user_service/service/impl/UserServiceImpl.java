package com.example.user_service.service.impl;

import com.example.user_service.constants.RoleConstants;
import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.Company;
import com.example.user_service.entity.UserEntity;
import com.example.user_service.exception.ResourceNotFoundException;
import com.example.user_service.exception.UserAlreadyExistsException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.ICompanyService;
import com.example.user_service.service.IUserService;
import com.example.user_service.service.client.IdentityFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ICompanyService companyService;
    private final PasswordEncoder passwordEncoder;
    private final IdentityFeignClient identityFeignClient;

    @Override
    @Transactional
    public void registerUser(UserDto userDto) {
        checkIfUserExists(userDto);

        var companyDto = userDto.getCompany();
        Company company = null;
        if (companyDto != null) {
            company = companyService.registerCompany(companyDto);
        }

        UserEntity user = UserMapper.INSTANCE.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setCompany(company);
        var role = roleRepository.getRoleByName(RoleConstants.USER_ROLE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Role", "role", RoleConstants.USER_ROLE));
        user.setRoles(List.of(role));

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
    public boolean updateUser(UserDto userDto, String authorizationToken) {
        String currentUserEmail = identityFeignClient.extractEmail(authorizationToken).getBody();

        UserEntity existingUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUserEmail));

        if (!userDto.getEmail().equals(currentUserEmail)) {
            checkIfUserExistsByEmail(userDto.getEmail());
        }

        if (!userDto.getMobileNumber().equals(existingUser.getMobileNumber())) {
            checkIfUserExistsByMobileNumber(userDto.getMobileNumber());
        }

        existingUser.setFullName(userDto.getFullName());
        existingUser.setMobileNumber(userDto.getMobileNumber());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(existingUser);

        return true;
    }

    @Override
    @Transactional
    public boolean deleteUser(String email) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        existingUser.getRoles().clear();
        userRepository.save(existingUser);

        userRepository.deleteById(existingUser.getId());
        return true;
    }

    private void checkIfUserExists(UserDto userDto) {
        checkIfUserExistsByEmail(userDto.getEmail());
        checkIfUserExistsByMobileNumber(userDto.getMobileNumber());
    }

    private void checkIfUserExistsByEmail(String email) {
        Optional<UserEntity> userByEmail = userRepository.findByEmail(email);

        if (userByEmail.isPresent()) {
            throw new UserAlreadyExistsException("User already registered with provided email");
        }
    }

    private void checkIfUserExistsByMobileNumber(String mobileNumber) {
        Optional<UserEntity> userByMobileNumber = userRepository.findByMobileNumber(mobileNumber);

        if (userByMobileNumber.isPresent()) {
            throw new UserAlreadyExistsException("User already registered with provided mobile number");
        }
    }
}
