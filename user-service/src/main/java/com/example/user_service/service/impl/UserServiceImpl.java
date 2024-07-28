package com.example.user_service.service.impl;

import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
import com.example.user_service.exception.ResourceNotFoundException;
import com.example.user_service.exception.UserAlreadyExistsException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.IUserService;
import lombok.RequiredArgsConstructor;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerUser(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User already registered with given email " +
                    userDto.getEmail());
        }
        User user = UserMapper.mapToUser(userDto, new User());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.getRoleByName("USER");
        user.setRoles(List.of(role));

        userRepository.save(user);
    }

    @Override
    public UserDto fetchUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return UserMapper.mapToUserDto(user, new UserDto());
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
