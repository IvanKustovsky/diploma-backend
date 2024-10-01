package com.e2rent.user_service.mapper;

import com.e2rent.user_service.dto.RegisterUserDto;
import com.e2rent.user_service.dto.UpdateUserDto;
import com.e2rent.user_service.dto.UserDto;
import com.e2rent.user_service.entity.UserEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(UserEntity user);

    @Mapping(target = "fullName", expression = "java(registerUserDto.getFirstName() +" +
            " ' ' + registerUserDto.getLastName())")
    UserEntity toEntity(RegisterUserDto registerUserDto);

    @Mapping(target = "fullName", expression = "java(updateUserDto.getFirstName() +" +
            " ' ' + updateUserDto.getLastName())")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserEntityFromDto(UpdateUserDto updateUserDto, @MappingTarget UserEntity existingUser);
}
