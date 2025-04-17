package com.e2rent.equipment.service;

import com.e2rent.equipment.dto.EquipmentDto;
import com.e2rent.equipment.dto.EquipmentSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IEquipmentService {

    /**
     * @param equipmentDto - EquipmentDto Object
     * @param file - Image file
     */
    void registerEquipment(EquipmentDto equipmentDto, MultipartFile file, String authorizationToken);

    /**
     * @param equipmentId - EquipmentDto Object
     */
    EquipmentDto fetchEquipment(Long equipmentId);

    /**
     * @param equipmentId - EquipmentDto Object
     * @param equipmentDto - EquipmentDto Object
     */
    void updateEquipment(Long equipmentId, EquipmentDto equipmentDto, String authorizationToken);

    /**
     * @param equipmentId - EquipmentId Object
     */
    void deleteEquipment(Long equipmentId); // TODO

    /**
     * @return list of all equipments with main image uploaded
     */
    Page<EquipmentSummaryDto> findAllEquipmentsWithImage(Pageable pageable);

    /**
     * Отримати все обладнання, додане конкретним користувачем
     *
     * @param authorizationToken - ID користувача
     * @param pageable - об'єкт для пагінації
     * @return сторінка (Page) з оголошеннями користувача у форматі EquipmentSummaryDto
     */
    Page<EquipmentSummaryDto> findEquipmentsByUser(String authorizationToken, Pageable pageable);

    /**
     * @param equipmentId - EquipmentId Object
     * @param file - Main image file
     */
    void uploadMainImage(Long equipmentId, MultipartFile file, String authorizationToken);

    /**
     * @param equipmentId - EquipmentId Object
     * @param files - List of images
     */
    void uploadImages(Long equipmentId, List<MultipartFile> files, String authorizationToken);

    /**
     * @param imageId - ID зображення
     * @return байти зображення
     */
    byte[] downloadImage(Long imageId);
}

