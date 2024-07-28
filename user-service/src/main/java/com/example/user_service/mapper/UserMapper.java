package com.example.user_service.mapper;

import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.User;

public class UserMapper {

    public static UserDto mapToUserDto(User user, UserDto userDto) {
        userDto.setEmail(user.getEmail());
        userDto.setCompany(user.isCompany());
        userDto.setFullName(user.getFullName());
        userDto.setMobileNumber(user.getMobileNumber());
        userDto.setRoleId(user.getRoleId());
        return userDto;
    }

    public static User mapToUser(UserDto userDto, User user) {
        user.setEmail(userDto.getEmail());
        user.setCompany(userDto.isCompany());
        user.setFullName(userDto.getFullName());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setRoleId(userDto.getRoleId());
        user.setPassword(userDto.getPassword());
        return user;
    }
}
