package com.example.equipment.mapper;

import com.example.equipment.dto.EquipmentDto;
import com.example.equipment.entity.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EquipmentMapper {

    EquipmentMapper INSTANCE = Mappers.getMapper(EquipmentMapper.class);

    Equipment toEquipment(EquipmentDto equipmentDto);

    @Mapping(target = "mainImageUrl", source = "mainImage.name")
    @Mapping(target = "imageUrls", expression = "java(equipment.getImages().stream().map(Image::getName).collect(java.util.stream.Collectors.toList()))")
    EquipmentDto toEquipmentDto(Equipment equipment);
}
