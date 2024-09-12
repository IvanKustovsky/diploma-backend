package com.example.equipment.service.impl;

import com.example.equipment.dto.EquipmentDto;
import com.example.equipment.dto.EquipmentSummaryDto;
import com.example.equipment.entity.Equipment;
import com.example.equipment.entity.Image;
import com.example.equipment.exception.ImageLimitExceededException;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.repository.EquipmentRepository;
import com.example.equipment.service.ImageService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Equipment Service Test Class")
class EquipmentServiceImplTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private EquipmentServiceImpl equipmentServiceImpl;

    private static final int MAX_IMAGE_LIMIT = 5;

    @Test
    @Order(1)
    void registerEquipment() {
        // given
        EquipmentDto equipmentDto = new EquipmentDto();
        equipmentDto.setName("Test Equipment");

        Equipment equipment = new Equipment();
        equipment.setEquipmentId(-11L);
        equipment.setName("Test Equipment");

        MultipartFile file = Mockito.mock(MultipartFile.class);

        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
        when(file.isEmpty()).thenReturn(false);

        Image mainImage = new Image();
        when(imageService.uploadImage(file, equipment)).thenReturn(mainImage);

        // when
        equipmentServiceImpl.registerEquipment(equipmentDto, file);

        // then
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
        verify(imageService, times(1)).uploadImage(file, equipment);
    }

    @Test
    @Order(2)
    void registerEquipmentWithoutMainImage() {
        // given
        EquipmentDto equipmentDto = new EquipmentDto();
        equipmentDto.setName("Test Equipment");

        Equipment equipment = new Equipment();
        equipment.setEquipmentId(-11L);
        equipment.setName("Test Equipment");

        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);

        // when
        equipmentServiceImpl.registerEquipment(equipmentDto, null);

        // then
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
        verify(imageService, never()).uploadImage(null, equipment);
    }

    @Test
    @Order(3)
    void registerEquipmentWhenMainImageIsEmpty() {
        // given
        EquipmentDto equipmentDto = new EquipmentDto();
        equipmentDto.setName("Test Equipment");

        Equipment equipment = new Equipment();
        equipment.setEquipmentId(-11L);
        equipment.setName("Test Equipment");

        MultipartFile file = Mockito.mock(MultipartFile.class);

        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
        when(file.isEmpty()).thenReturn(true);

        // when
        equipmentServiceImpl.registerEquipment(equipmentDto, file);

        // then
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
        verify(imageService, never()).uploadImage(file, equipment);
    }

    @Test
    @Order(4)
    void fetchEquipment() {
        // given
        Long equipmentId = 1L;
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentId);
        equipment.setName("Test Equipment");

        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));

        // when
        EquipmentDto result = equipmentServiceImpl.fetchEquipment(equipmentId);

        // then
        assertNotNull(result);
        assertEquals("Test Equipment", result.getName()); // Перевіряємо правильне поле
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
    }

    @Test
    @Order(5)
    void fetchNotExistingEquipmentThrowsException() {
        // given
        Long equipmentId = 1L;

        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> equipmentServiceImpl.fetchEquipment(equipmentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "Equipment", "ID", equipmentId));

        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
    }

    @Test
    @Order(6)
    void updateEquipment() {
        // given
        Long equipmentId = 1L;
        EquipmentDto equipmentDto = new EquipmentDto();
        equipmentDto.setName("Updated Equipment");

        Equipment existingEquipment = new Equipment();
        existingEquipment.setEquipmentId(equipmentId);
        existingEquipment.setName("Old Equipment");

        MultipartFile file = Mockito.mock(MultipartFile.class);

        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(existingEquipment));
        when(file.isEmpty()).thenReturn(false);

        // when
        equipmentServiceImpl.updateEquipment(equipmentId, equipmentDto, file);

        // then
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, times(1)).uploadImage(file, existingEquipment);
    }

    @Test
    @Order(7)
    void updateNotExistingEquipmentThrowsException() {
        // given
        Long equipmentId = 1L;
        EquipmentDto equipmentDto = new EquipmentDto();
        equipmentDto.setName("Updated Equipment");

        Equipment existingEquipment = new Equipment();
        existingEquipment.setEquipmentId(equipmentId);
        existingEquipment.setName("Old Equipment");

        MultipartFile file = Mockito.mock(MultipartFile.class);

        // when, then
        assertThatThrownBy(() -> equipmentServiceImpl.updateEquipment(equipmentId, equipmentDto, file))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "Equipment", "ID", equipmentId));

        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, never()).uploadImage(file, existingEquipment);
    }

    @Test
    @Order(8)
    void deleteEquipment() {
        // given
        Long equipmentId = 1L;
        Equipment existingEquipment = new Equipment();
        existingEquipment.setEquipmentId(equipmentId);

        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(existingEquipment));

        // when
        equipmentServiceImpl.deleteEquipment(equipmentId);

        // then
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(equipmentRepository, times(1)).deleteById(equipmentId);
    }

    @Test
    @Order(9)
    void deleteNotExistingEquipmentThrowsException() {
        // given
        Long equipmentId = 1L;
        Equipment existingEquipment = new Equipment();
        existingEquipment.setEquipmentId(equipmentId);

        // when, then
        assertThatThrownBy(() -> equipmentServiceImpl.deleteEquipment(equipmentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "Equipment", "ID", equipmentId));

        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(equipmentRepository, never()).deleteById(equipmentId);
    }

    @Test
    @Order(10)
    void findAllEquipmentsWithImage() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<EquipmentSummaryDto> page = new PageImpl<>(Collections.emptyList());

        when(equipmentRepository.findAllWithMainImage(pageable)).thenReturn(page);

        // when
        Page<EquipmentSummaryDto> result = equipmentServiceImpl.findAllEquipmentsWithImage(pageable);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(equipmentRepository, times(1)).findAllWithMainImage(pageable);
    }

    @Test
    @Order(11)
    void uploadMainImage() {
        // given
        Long equipmentId = 1L;
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentId);
        MultipartFile file = mock(MultipartFile.class);

        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));

        // when
        equipmentServiceImpl.uploadMainImage(equipmentId, file);

        // then
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, times(1)).uploadImage(file, equipment);
    }

    @Test
    @Order(12)
    void uploadMainImageWhenEquipmentNotExist() {
        // given
        Long equipmentId = 1L;
        Equipment equipment = new Equipment();
        MultipartFile file = mock(MultipartFile.class);

        // when, then
        assertThatThrownBy(() -> equipmentServiceImpl.uploadMainImage(equipmentId, file))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "Equipment", "ID", equipmentId));

        // then
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, never()).uploadImage(file, equipment);
    }

    @Test
    @Order(13)
    void uploadNullAsMainImage() {
        // given
        Long equipmentId = 1L;
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentId);

        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));

        // when
        equipmentServiceImpl.uploadMainImage(equipmentId, null);

        // then
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, never()).uploadImage(null, equipment);
    }

    @Test
    @Order(14)
    void uploadEmptyFileAsMainImage() {
        // given
        Long equipmentId = 1L;
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentId);
        MultipartFile file = mock(MultipartFile.class);

        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));
        when(file.isEmpty()).thenReturn(true);

        // when
        equipmentServiceImpl.uploadMainImage(equipmentId, file);

        // then
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, never()).uploadImage(file, equipment);
    }

    @Test
    @Order(15)
    void uploadMainImageWithReplaceOldOne() {
        // given
        Long equipmentId = 1L;
        Long imageId = 100L;
        Equipment equipment = mock(Equipment.class);
        MultipartFile file = mock(MultipartFile.class);

        Image mainImage = mock(Image.class);
        Image updatedMainImage = mock(Image.class);

        when(mainImage.getId()).thenReturn(imageId);
        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));
        when(equipment.getMainImage()).thenReturn(mainImage);

        when(imageService.uploadImage(file, equipment)).thenReturn(updatedMainImage);

        // when
        equipmentServiceImpl.uploadMainImage(equipmentId, file);

        // then
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, times(1)).deleteImage(imageId);
        verify(imageService, times(1)).uploadImage(file, equipment);
        verify(equipment, times(1)).setMainImage(updatedMainImage);
    }

    @Test
    @Order(16)
    void uploadImages() {
        // given
        Long equipmentId = 1L;
        Equipment equipment = spy(new Equipment());
        equipment.setEquipmentId(equipmentId);

        // Моделюємо Image для повернення
        Image image1 = new Image();
        Image image2 = new Image();

        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));
        when(imageService.uploadImage(any(MultipartFile.class), eq(equipment)))
                .thenReturn(image1)
                .thenReturn(image2);

        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        List<MultipartFile> files = List.of(file1, file2);

        // when
        equipmentServiceImpl.uploadImages(equipmentId, files);

        // then
        verify(equipmentRepository, times(2)).findEquipmentById(equipmentId);
        verify(imageService, times(files.size())).uploadImage(any(MultipartFile.class), eq(equipment));
        verify(equipment, times(files.size())).addImage(any(Image.class));
    }

    @Test
    @Order(17)
    void addImagesToEquipmentThrowsException() {
        // given
        Long equipmentId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        Equipment equipment = mock(Equipment.class);

        List<Image> images = new ArrayList<>();
        for (int i = 0; i < MAX_IMAGE_LIMIT; i++) {
            images.add(mock(Image.class));
        }

        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));
        when(equipment.getImages()).thenReturn(images);

        // when, then
        assertThatThrownBy(() -> equipmentServiceImpl.uploadImages(equipmentId, List.of(file)))
                .isInstanceOf(ImageLimitExceededException.class)
                .hasMessageContaining(String.format("Reached maximum limit (" + MAX_IMAGE_LIMIT + ")" +
                        " of images per one equipment."));

        verify(imageService, never()).uploadImage(file, equipment);
    }
}