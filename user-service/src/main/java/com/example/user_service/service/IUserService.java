package com.example.user_service.service;

import com.example.user_service.dto.LoginDto;
import com.example.user_service.dto.UserDto;

public interface IUserService {

    void registerUser(UserDto userDto);

    void login(LoginDto loginDto);

    UserDto fetchUser(String email);

    boolean updateUser(UserDto userDto);

    boolean deleteUser(String email);

    // TODO: Add java docs and perhaps change some method signatures
}
