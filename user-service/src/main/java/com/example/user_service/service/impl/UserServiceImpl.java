package com.example.user_service.service.impl;

import com.example.user_service.constants.RoleConstants;
import com.example.user_service.dto.LoginDto;
import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.Company;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
import com.example.user_service.exception.ResourceNotFoundException;
import com.example.user_service.exception.UserAlreadyExistsException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.ICompanyService;
import com.example.user_service.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// TODO: Implement this class and add java docs

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ICompanyService companyService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public void registerUser(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()) { // TODO: Check for password and mobileNumber
            throw new UserAlreadyExistsException("User already registered with given email " +
                    userDto.getEmail());
        }

        var companyDto = userDto.getCompany();
        Company company = null;
        if (companyDto != null) {
            company = companyService.registerCompany(companyDto);
        }

        User user = UserMapper.INSTANCE.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setCompany(company);
        Role role = roleRepository.getRoleByName(RoleConstants.USER_ROLE)
                .orElseGet(() -> {
                    Role newRole = Role.builder().name(RoleConstants.USER_ROLE).build();
                    return roleRepository.save(newRole);
                });
        user.setRoles(List.of(role));

        userRepository.save(user);
    }

    @Override
    public void login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public UserDto fetchUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    public boolean updateUser(UserDto userDto) {
        return false;
    }

    @Override
    public boolean deleteUser(String email) {
        return false;
    }
}
