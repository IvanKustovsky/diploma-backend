package com.example.equipment.service;

import com.example.equipment.dto.EquipmentDto;

public interface IEquipmentService {

    /**
     *
     * @param equipmentDto - EquipmentDto Object
     */
    void registerEquipment(EquipmentDto equipmentDto);

    /**
     *
     * @param equipmentId - EquipmentDto Object
     */
    void fetchEquipment(Long equipmentId);

    /**
     *
     * @param equipmentId - EquipmentDto Object
     * @param equipmentDto - EquipmentDto Object
     * @return boolean indicating if the update of Equipment is successful or not
     */
    boolean updateEquipment(Long equipmentId, EquipmentDto equipmentDto);

    /**
     *
     * @param equipmentId - EquipmentId Object
     * @return boolean indicating if the delete of Equipment is successful or not
     */
    boolean deleteEquipment(Long equipmentId);
}
