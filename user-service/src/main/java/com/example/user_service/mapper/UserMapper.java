package com.example.user_service.mapper;

import com.example.user_service.dto.CompanyDto;
import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.Company;
import com.example.user_service.entity.User;

import java.util.Objects;

public class UserMapper {

    public static UserDto mapToUserDto(User user, UserDto userDto) {
        if(Objects.isNull(user) || Objects.isNull(userDto)) { // TODO: Do something about null
            return null;
        }
        userDto.setEmail(user.getEmail());
        userDto.setCompanyDto(CompanyMapper.mapToCompanyDto(user.getCompany(), new CompanyDto()));
        userDto.setFullName(user.getFullName());
        userDto.setMobileNumber(user.getMobileNumber());
        userDto.setRoles(user.getRoles());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    public static User mapToUser(UserDto userDto, User user) {
        if(Objects.isNull(user) || Objects.isNull(userDto)) {
            return null;
        }
        user.setEmail(userDto.getEmail());
        user.setCompany(CompanyMapper.mapToCompany(userDto.getCompanyDto(), new Company()));
        user.setFullName(userDto.getFullName());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setRoles(userDto.getRoles());
        user.setPassword(userDto.getPassword());
        return user;
    }
}
