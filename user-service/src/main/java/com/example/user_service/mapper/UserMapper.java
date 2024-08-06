package com.example.user_service.mapper;

import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(UserEntity user);

    UserEntity toEntity(UserDto userDto);
}
