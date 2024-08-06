package com.example.equipment.service.impl;

import com.example.equipment.dto.EquipmentDto;
import com.example.equipment.repository.EquipmentRepository;
import com.example.equipment.service.IEquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements IEquipmentService {

    private final EquipmentRepository equipmentRepository;

    // TODO: Implement this class

    @Override
    public void registerEquipment(EquipmentDto equipmentDto) {

    }

    @Override
    public void fetchEquipment(Long equipmentId) {

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
