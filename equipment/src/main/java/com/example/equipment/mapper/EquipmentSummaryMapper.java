package com.example.equipment.mapper;

import com.example.equipment.dto.EquipmentSummaryDto;
import com.example.equipment.entity.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EquipmentSummaryMapper {
    EquipmentSummaryMapper INSTANCE = Mappers.getMapper(EquipmentSummaryMapper.class);

    @Mapping(source = "equipmentId", target = "id")
    @Mapping(source = "mainImage.id", target = "mainImageId")
    EquipmentSummaryDto toEquipmentSummaryDto(Equipment equipment);
}
