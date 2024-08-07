package com.example.equipment.service.impl;

import com.example.equipment.dto.EquipmentDto;
import com.example.equipment.entity.Equipment;
import com.example.equipment.entity.Image;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.mapper.EquipmentMapper;
import com.example.equipment.repository.EquipmentRepository;
import com.example.equipment.service.IEquipmentService;
import com.example.equipment.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements IEquipmentService {

    private final EquipmentRepository equipmentRepository;

    private final ImageService imageService;

    // TODO: Implement this class

    @Override
    @Transactional
    public void registerEquipment(EquipmentDto equipmentDto) {
        Equipment equipment = EquipmentMapper.INSTANCE.toEquipment(equipmentDto);

        equipmentRepository.save(equipment);
    }

    @Override
    @Transactional
    public void registerEquipmentWithMainImage(EquipmentDto equipmentDto, MultipartFile file) throws IOException {
        Equipment equipment = EquipmentMapper.INSTANCE.toEquipment(equipmentDto);

        Equipment savedEquipment = equipmentRepository.save(equipment);

        Image mainImage = imageService.uploadImage(file, savedEquipment);
        savedEquipment.setMainImage(mainImage);
        equipmentRepository.save(savedEquipment);
    }

    @Override
    @Transactional(readOnly = true)
    public EquipmentDto fetchEquipment(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        return EquipmentMapper.INSTANCE.toEquipmentDto(equipment);
    }

    @Override
    public boolean updateEquipment(Long equipmentId, EquipmentDto equipmentDto) {
        return false;
    }

    @Override
    public boolean deleteEquipment(Long equipmentId) {
        return false;
    }
}
