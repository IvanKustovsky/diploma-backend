package com.example.equipment.service.impl;

import com.example.equipment.dto.EquipmentDto;
import com.example.equipment.dto.EquipmentSummaryDto;
import com.example.equipment.entity.Equipment;
import com.example.equipment.entity.Image;
import com.example.equipment.exception.ImageLimitExceededException;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.mapper.EquipmentMapper;
import com.example.equipment.repository.EquipmentRepository;
import com.example.equipment.service.IEquipmentService;
import com.example.equipment.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private static final int MAX_IMAGE_LIMIT = 5;

    @Override
    @Transactional
    public void registerEquipment(EquipmentDto equipmentDto, MultipartFile file) {
        Equipment equipment = EquipmentMapper.INSTANCE.toEquipment(equipmentDto);

        Equipment savedEquipment = equipmentRepository.save(equipment);

        if (file != null && !file.isEmpty()) {
            Image mainImage = imageService.uploadImage(file, savedEquipment);
            savedEquipment.setMainImage(mainImage);
        }
    }

    @Override
    public EquipmentDto fetchEquipment(Long equipmentId) {
        var equipment = equipmentRepository.findEquipmentById(equipmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        return EquipmentMapper.INSTANCE.toEquipmentDto(equipment);
    }

    @Override
    @Transactional
    public void updateEquipment(Long equipmentId, EquipmentDto equipmentDto, MultipartFile file) {
        Equipment equipment = equipmentRepository.findEquipmentById(equipmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        EquipmentMapper.INSTANCE.updateEquipmentFromDto(equipmentDto, equipment);

        uploadMainImage(file, equipment);
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
    @Transactional
    public void uploadMainImage(Long equipmentId, MultipartFile file) {
        Equipment equipment = equipmentRepository.findEquipmentById(equipmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

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
    public void uploadImages(Long equipmentId, List<MultipartFile> files) {
        files.forEach(image -> addImageToEquipment(equipmentId, image));
    }

    private void addImageToEquipment(Long equipmentId, MultipartFile image) {
        Equipment equipment = equipmentRepository.findEquipmentById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        if (equipment.getImages().size() >= MAX_IMAGE_LIMIT) {
            throw new ImageLimitExceededException("Reached maximum limit (" + MAX_IMAGE_LIMIT + ")" +
                    " of images per one equipment.");
        }

        if (image != null && !image.isEmpty()) {
            var uploadedImage = imageService.uploadImage(image, equipment);
            equipment.addImage(uploadedImage);
        }
    }
}
