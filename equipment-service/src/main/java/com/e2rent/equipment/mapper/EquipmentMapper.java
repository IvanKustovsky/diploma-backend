package com.e2rent.equipment.mapper;

import com.e2rent.equipment.dto.EquipmentDto;
import com.e2rent.equipment.entity.Equipment;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EquipmentMapper {

    EquipmentMapper INSTANCE = Mappers.getMapper(EquipmentMapper.class);

    Equipment toEquipment(EquipmentDto equipmentDto);

    @Mapping(target = "id", source = "equipmentId")
    @Mapping(target = "mainImageId", source = "mainImage.id")
    @Mapping(target = "imageIds", expression = "java(equipment.getImages().stream().map(image -> image.getId())" +
            ".collect(java.util.stream.Collectors.toList()))")
    EquipmentDto toEquipmentDto(Equipment equipment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userId", ignore = true)
    void updateEquipmentFromDto(EquipmentDto equipmentDto, @MappingTarget Equipment equipment);
}
