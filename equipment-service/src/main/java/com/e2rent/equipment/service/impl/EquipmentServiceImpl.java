package com.e2rent.equipment.service.impl;

import com.e2rent.equipment.dto.EquipmentDto;
import com.e2rent.equipment.dto.EquipmentSummaryDto;
import com.e2rent.equipment.entity.Equipment;
import com.e2rent.equipment.entity.Image;
import com.e2rent.equipment.enums.EquipmentStatus;
import com.e2rent.equipment.exception.ImageLimitExceededException;
import com.e2rent.equipment.exception.ResourceNotFoundException;
import com.e2rent.equipment.mapper.EquipmentMapper;
import com.e2rent.equipment.repository.EquipmentRepository;
import com.e2rent.equipment.service.IEquipmentService;
import com.e2rent.equipment.service.ImageService;
import com.e2rent.equipment.service.client.UsersFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final UsersFeignClient usersFeignClient;
    private static final int MAX_IMAGE_LIMIT = 5;

    @Override
    @Transactional
    public void registerEquipment(EquipmentDto equipmentDto, MultipartFile file, String authorizationToken) {
        var currentUserId = usersFeignClient.getUserIdFromToken(authorizationToken).getBody();
        equipmentDto.setUserId(currentUserId);
        Equipment equipment = EquipmentMapper.INSTANCE.toEquipment(equipmentDto);
        equipment.setStatus(EquipmentStatus.ACTIVE);
        Equipment savedEquipment = equipmentRepository.save(equipment);

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
    public void updateEquipment(Long equipmentId, EquipmentDto equipmentDto, String authorizationToken) {
        var currentUserId = usersFeignClient.getUserIdFromToken(authorizationToken).getBody();
        Equipment equipment = equipmentRepository.findEquipmentById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        if (!equipment.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("Ви не можете редагувати чуже обладнання.");
        }

        EquipmentMapper.INSTANCE.updateEquipmentFromDto(equipmentDto, equipment);
    }

    @Override
    @Transactional
    public void deleteEquipment(Long equipmentId) {
        equipmentRepository.findEquipmentById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        equipmentRepository.deleteById(equipmentId);
    }

    @Override
    public Page<EquipmentSummaryDto> findAllEquipmentsWithImage(Pageable pageable) {
        return equipmentRepository.findAllWithMainImage(pageable);
    }

    @Override
    public Page<EquipmentSummaryDto> findEquipmentsByUser(String authorizationToken, Pageable pageable) {
        var currentUserId = usersFeignClient.getUserIdFromToken(authorizationToken).getBody();
        return equipmentRepository.findAllByUserId(currentUserId, pageable);
    }

    @Override
    @Transactional
    public void uploadMainImage(Long equipmentId, MultipartFile file, String authorizationToken) {
        var currentUserId = usersFeignClient.getUserIdFromToken(authorizationToken).getBody();
        Equipment equipment = equipmentRepository.findEquipmentById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        if (!equipment.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("Ви не можете редагувати чуже обладнання.");
        }

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
    public void uploadImages(Long equipmentId, List<MultipartFile> files, String authorizationToken) {
        var currentUserId = usersFeignClient.getUserIdFromToken(authorizationToken).getBody();
        files.forEach(image -> addImageToEquipment(equipmentId, image, currentUserId));
    }

    @Override
    public byte[] downloadImage(Long imageId) {
        return imageService.downloadImage(imageId);
    }

    private void addImageToEquipment(Long equipmentId, MultipartFile image, Long currentUserId) {
        Equipment equipment = equipmentRepository.findEquipmentById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        // Перевірка на власника
        if (!equipment.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("Ви не можете редагувати чуже обладнання.");
        }

        if (equipment.getImages().size() >= MAX_IMAGE_LIMIT) {
            throw new ImageLimitExceededException("Досягнуто ліміт (" + MAX_IMAGE_LIMIT
                    + ") зображень для одного обладнання.");
        }

        if (image != null && !image.isEmpty()) {
            var uploadedImage = imageService.uploadImage(image, equipment);
            equipment.addImage(uploadedImage);
        }
    }
}
