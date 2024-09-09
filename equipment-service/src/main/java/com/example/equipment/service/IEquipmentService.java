package com.example.equipment.service;

import com.example.equipment.dto.EquipmentDto;
import com.example.equipment.dto.EquipmentSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IEquipmentService {

    /**
     *
     * @param equipmentDto - EquipmentDto Object
     * @param file - Image file
     */
    void registerEquipment(EquipmentDto equipmentDto, MultipartFile file);

    /**
     *
     * @param equipmentId - EquipmentDto Object
     */
    EquipmentDto fetchEquipment(Long equipmentId);

    /**
     *
     * @param equipmentId - EquipmentDto Object
     * @param equipmentDto - EquipmentDto Object
     */
    void updateEquipment(Long equipmentId, EquipmentDto equipmentDto, MultipartFile file);

    /**
     *
     * @param equipmentId - EquipmentId Object
     */
    void deleteEquipment(Long equipmentId);

    /**
     *
     * @return list of all equipments with main image uploaded
     */
    Page<EquipmentSummaryDto> findAllEquipmentsWithImage(Pageable pageable);

    /**
     *
     * @param equipmentId - - EquipmentId Object
     * @param file - Main image file
     */
    void uploadMainImage(Long equipmentId, MultipartFile file);

    /**
     *
     * @param equipmentId - - EquipmentId Object
     * @param files - List of images
     */
    void uploadImages(Long equipmentId, List<MultipartFile> files);
}
