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
        checkIfUserExists(userDto);

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
    @Transactional
    public boolean updateUser(UserDto userDto) {
        User existingUser = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userDto.getEmail()));

        if(!userDto.getEmail().equals(existingUser.getEmail()) ||
         !userDto.getMobileNumber().equals(existingUser.getMobileNumber())) {
            checkIfUserExists(userDto);
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
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        existingUser.getRoles().clear();
        userRepository.save(existingUser);

        userRepository.deleteById(existingUser.getId());
        return true;
    }

    private void checkIfUserExists(UserDto userDto) {
        Optional<User> userByEmail = userRepository.findByEmail(userDto.getEmail());
        Optional<User> userByMobileNumber = userRepository.findByMobileNumber(userDto.getMobileNumber());

        if (userByEmail.isPresent() || userByMobileNumber.isPresent()) {
            throw new UserAlreadyExistsException("User already registered with provided email or mobile number");
        }
    }
}
