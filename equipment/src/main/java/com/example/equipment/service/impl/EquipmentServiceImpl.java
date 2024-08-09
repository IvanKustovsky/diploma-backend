package com.example.equipment.service.impl;

import com.example.equipment.dto.EquipmentDto;
import com.example.equipment.dto.EquipmentSummaryDto;
import com.example.equipment.entity.Equipment;
import com.example.equipment.entity.Image;
import com.example.equipment.exception.ImageLimitExceededException;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.mapper.EquipmentMapper;
import com.example.equipment.mapper.EquipmentSummaryMapper;
import com.example.equipment.repository.EquipmentRepository;
import com.example.equipment.service.IEquipmentService;
import com.example.equipment.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements IEquipmentService {

    private final EquipmentRepository equipmentRepository;

    private final ImageService imageService;

    private static final int MAX_IMAGE_COUNT = 5;

    @Override
    @Transactional
    public void registerEquipment(EquipmentDto equipmentDto, MultipartFile file) {
        Equipment equipment = EquipmentMapper.INSTANCE.toEquipment(equipmentDto);

        Equipment savedEquipment = equipmentRepository.save(equipment);

        if (file != null && !file.isEmpty()) {
            Image mainImage = imageService.uploadImage(file, savedEquipment);
            savedEquipment.setMainImage(mainImage);

            equipmentRepository.save(savedEquipment);
        }
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
    @Transactional
    public void updateEquipment(Long equipmentId, EquipmentDto equipmentDto, MultipartFile file) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        equipment.setCategory(equipmentDto.getCategory());
        equipment.setName(equipmentDto.getName());
        equipment.setDescription(equipmentDto.getDescription());
        equipment.setCondition(equipmentDto.getCondition());
        equipment.setPrice(equipmentDto.getPrice());

        uploadMainImage(file, equipment);

        equipmentRepository.save(equipment);
    }

    @Override
    @Transactional
    public void deleteEquipment(Long equipmentId) {
        equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        equipmentRepository.deleteById(equipmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentSummaryDto> findAllEquipmentsWithImage() {
        List<Equipment> equipments = equipmentRepository.findAllByMainImageNotNull();
        return equipments.stream()
                .map(EquipmentSummaryMapper.INSTANCE::toEquipmentSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public void uploadMainImage(Long equipmentId, MultipartFile file) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        uploadMainImage(file, equipment);

        equipmentRepository.save(equipment);
    }

    private void uploadMainImage(MultipartFile file, Equipment equipment) {
        if (file != null && !file.isEmpty()) {
            if(equipment.getMainImage() != null) {
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
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "ID", String.valueOf(equipmentId)));

        if (equipment.getImages().size() >= MAX_IMAGE_COUNT) {
            throw new ImageLimitExceededException("Cannot add more than " + MAX_IMAGE_COUNT + " images.");
        }

        if (image != null && !image.isEmpty()) {
            var uploadedImage = imageService.uploadImage(image, equipment);
            equipment.getImages().add(uploadedImage);
        }

        equipmentRepository.save(equipment);
    }
}
