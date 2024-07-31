package com.example.user_service.mapper;

import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
