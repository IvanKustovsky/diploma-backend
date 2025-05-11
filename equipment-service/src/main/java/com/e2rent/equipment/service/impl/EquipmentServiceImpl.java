package com.e2rent.equipment.service.impl;

import com.e2rent.equipment.dto.EquipmentDto;
import com.e2rent.equipment.entity.Equipment;
import com.e2rent.equipment.entity.Image;
import com.e2rent.equipment.enums.EquipmentStatus;
import com.e2rent.equipment.exception.ImageLimitExceededException;
import com.e2rent.equipment.exception.ResourceNotFoundException;
import com.e2rent.equipment.mapper.EquipmentMapper;
import com.e2rent.equipment.repository.EquipmentRepository;
import com.e2rent.equipment.service.IAdvertisementService;
import com.e2rent.equipment.service.IEquipmentService;
import com.e2rent.equipment.service.ImageService;
import com.e2rent.equipment.service.client.UsersFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EquipmentServiceImpl implements IEquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ImageService imageService;
    private final IAdvertisementService advertisementService;
    private final UsersFeignClient usersFeignClient;
    private static final int MAX_IMAGE_LIMIT = 5;

    @Override
    @Transactional
    public void registerEquipment(EquipmentDto equipmentDto, MultipartFile file, String authToken) {
        var currentUserId = usersFeignClient.getUserIdFromToken(authToken).getBody();
        equipmentDto.setUserId(currentUserId);
        Equipment equipment = EquipmentMapper.INSTANCE.toEquipment(equipmentDto);
        equipment.setStatus(EquipmentStatus.AVAILABLE);
        Equipment savedEquipment = equipmentRepository.save(equipment);

        advertisementService.createAdvertisement(savedEquipment);

        if (file != null && !file.isEmpty()) {
            Image mainImage = imageService.uploadImage(file, savedEquipment);
            savedEquipment.setMainImage(mainImage);
        }
    }

    @Override
    public EquipmentDto fetchEquipment(Long equipmentId) {
        var equipment = equipmentRepository.findEquipmentById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        return EquipmentMapper.INSTANCE.toEquipmentDto(equipment);
    }

    @Override
    @Transactional
    public void updateEquipment(Long equipmentId, EquipmentDto equipmentDto, String authToken) {
        var currentUserId = usersFeignClient.getUserIdFromToken(authToken).getBody();
        Equipment equipment = equipmentRepository.findEquipmentById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        if (!equipment.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("Ви не можете редагувати чуже обладнання.");
        }

        EquipmentMapper.INSTANCE.updateEquipmentFromDto(equipmentDto, equipment);

        advertisementService.markAsUpdated(equipmentId);
    }

    @Override
    @Transactional
    public void uploadMainImage(Long equipmentId, MultipartFile file, String authToken) {
        var currentUserId = usersFeignClient.getUserIdFromToken(authToken).getBody();
        Equipment equipment = equipmentRepository.findEquipmentById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        if (!equipment.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("Ви не можете редагувати чуже обладнання.");
        }
        advertisementService.markAsUpdated(equipmentId);
        uploadMainImage(file, equipment);
    }

    private void uploadMainImage(MultipartFile file, Equipment equipment) {
        if (file != null && !file.isEmpty()) {
            if (equipment.getMainImage() != null) {
                imageService.deleteImage(equipment.getMainImage().getId());
            }
            var updatedMainImage = imageService.uploadImage(file, equipment);
            equipment.setMainImage(updatedMainImage);
        }
    }

    @Override
    @Transactional
    public void uploadImages(Long equipmentId, List<MultipartFile> files, String authToken) {
        var currentUserId = usersFeignClient.getUserIdFromToken(authToken).getBody();
        files.forEach(image -> addImageToEquipment(equipmentId, image, currentUserId));
        advertisementService.markAsUpdated(equipmentId);
    }

    @Override
    public byte[] downloadImage(Long imageId) {
        return imageService.downloadImage(imageId);
    }

    @Override
    public Long getOwnerIdByEquipmentId(Long equipmentId) {
        return equipmentRepository.findOwnerIdByEquipmentId(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));
    }

    @Override
    @Transactional
    public void deactivateEquipmentById(Long equipmentId, String authToken) {
        Equipment equipment = getAuthorizedEquipment(equipmentId, authToken);
        equipment.setStatus(EquipmentStatus.INACTIVE);
    }

    @Override
    @Transactional
    public void activateEquipmentById(Long equipmentId, String authToken) {
        Equipment equipment = getAuthorizedEquipment(equipmentId, authToken);
        equipment.setStatus(EquipmentStatus.AVAILABLE);
    }

    private Equipment getAuthorizedEquipment(Long equipmentId, String authToken) {
        var currentUserId = usersFeignClient.getUserIdFromToken(authToken).getBody();

        var equipmentOwnerId = equipmentRepository.findOwnerIdByEquipmentId(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("UserId", "equipmentId", String.valueOf(equipmentId)));

        if (!equipmentOwnerId.equals(currentUserId)) {
            throw new AccessDeniedException("Ви не можете змінювати статус чужого обладнання.");
        }

        return equipmentRepository.findEquipmentById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "equipmentId", String.valueOf(equipmentId)));
    }

    private void addImageToEquipment(Long equipmentId, MultipartFile image, Long currentUserId) {
        Equipment equipment = equipmentRepository.findEquipmentById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        // Перевірка на власника
        if (!equipment.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("Ви не можете редагувати чуже обладнання.");
        }

        // Перевірка ліміту (враховує і головне зображення, і додаткові)
        int totalImages = equipment.getImages().size() + (equipment.getMainImage() != null ? 1 : 0);
        if (totalImages > MAX_IMAGE_LIMIT) {
            throw new ImageLimitExceededException("Досягнуто загальний ліміт ("
                    + MAX_IMAGE_LIMIT + ") зображень.");
        }

        if (image != null && !image.isEmpty()) {
            var uploadedImage = imageService.uploadImage(image, equipment);

            if (equipment.getMainImage() == null) {
                equipment.setMainImage(uploadedImage);
            } else {
                equipment.addImage(uploadedImage);
            }
        }
    }
}
