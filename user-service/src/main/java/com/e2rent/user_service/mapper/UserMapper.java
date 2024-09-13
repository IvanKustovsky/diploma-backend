package com.e2rent.user_service.mapper;

import com.e2rent.user_service.dto.UserDto;
import com.e2rent.user_service.entity.UserEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(UserEntity user);

    UserEntity toEntity(UserDto userDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUserEntityFromDto(UserDto userDto, @MappingTarget UserEntity existingUser);
}
