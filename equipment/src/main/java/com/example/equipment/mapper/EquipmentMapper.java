package com.example.equipment.mapper;

import com.example.equipment.dto.EquipmentDto;
import com.example.equipment.entity.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EquipmentMapper {

    EquipmentMapper INSTANCE = Mappers.getMapper(EquipmentMapper.class);

    Equipment toEquipment(EquipmentDto equipmentDto);

    EquipmentDto toEquipmentDto(Equipment equipment);
}
