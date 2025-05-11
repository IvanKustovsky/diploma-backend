package com.e2rent.equipment.service.impl;

import com.e2rent.equipment.dto.EquipmentDto;
import com.e2rent.equipment.entity.Equipment;
import com.e2rent.equipment.entity.Image;
import com.e2rent.equipment.enums.EquipmentStatus;
import com.e2rent.equipment.exception.ImageLimitExceededException;
import com.e2rent.equipment.exception.ResourceNotFoundException;
import com.e2rent.equipment.repository.EquipmentRepository;
import com.e2rent.equipment.service.IAdvertisementService;
import com.e2rent.equipment.service.ImageService;
import com.e2rent.equipment.service.client.UsersFeignClient;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @Mock
    private UsersFeignClient usersFeignClient;

    @Mock
    private IAdvertisementService advertisementService;

    @InjectMocks
    private EquipmentServiceImpl equipmentServiceImpl;

    private static final int MAX_IMAGE_LIMIT = 5;
    private static final String MOCK_TOKEN = "mockTokenValue";

    @Test
    @Order(1)
    void registerEquipment() {
        // given
        Long mockUserId = 123L;
        EquipmentDto equipmentDto = new EquipmentDto();

        Equipment equipment = new Equipment();

        MultipartFile file = Mockito.mock(MultipartFile.class);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
        doNothing().when(advertisementService).createAdvertisement(any(Equipment.class));
        when(file.isEmpty()).thenReturn(false);

        Image mainImage = new Image();
        when(imageService.uploadImage(file, equipment)).thenReturn(mainImage);

        // when
        equipmentServiceImpl.registerEquipment(equipmentDto, file, MOCK_TOKEN);

        // then
        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
        verify(imageService, times(1)).uploadImage(file, equipment);
    }

    @Test
    @Order(2)
    void registerEquipmentWithoutMainImage() {
        // given
        Long mockUserId = 123L;
        EquipmentDto equipmentDto = new EquipmentDto();
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(-11L);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
        doNothing().when(advertisementService).createAdvertisement(any(Equipment.class));

        // when
        equipmentServiceImpl.registerEquipment(equipmentDto, null, MOCK_TOKEN);

        // then
        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
        verify(imageService, never()).uploadImage(null, equipment);
    }

    @Test
    @Order(3)
    void registerEquipmentWhenMainImageIsEmpty() {
        // given
        Long mockUserId = 123L;
        EquipmentDto equipmentDto = new EquipmentDto();
        Equipment equipment = new Equipment();

        MultipartFile file = Mockito.mock(MultipartFile.class);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
        doNothing().when(advertisementService).createAdvertisement(any(Equipment.class));
        when(file.isEmpty()).thenReturn(true);

        // when
        equipmentServiceImpl.registerEquipment(equipmentDto, file, MOCK_TOKEN);

        // then
        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
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

        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));

        // when
        EquipmentDto result = equipmentServiceImpl.fetchEquipment(equipmentId);

        // then
        assertNotNull(result);
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
    }

    @Test
    @Order(5)
    void fetchNotExistingEquipmentThrowsResourceNotFoundException() {
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
        Long mockUserId = 123L;
        Long equipmentId = 1L;
        EquipmentDto equipmentDto = new EquipmentDto();
        equipmentDto.setName("Updated Equipment");

        Equipment existingEquipment = new Equipment();
        existingEquipment.setEquipmentId(equipmentId);
        existingEquipment.setName("Old Equipment");
        existingEquipment.setUserId(mockUserId);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));
        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(existingEquipment));
        doNothing().when(advertisementService).markAsUpdated(equipmentId);

        // when
        equipmentServiceImpl.updateEquipment(equipmentId, equipmentDto, MOCK_TOKEN);

        // then
        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
    }

    @Test
    @Order(7)
    void updateNotExistingEquipmentThrowsResourceNotFoundException() {
        // given
        Long mockUserId = 123L;
        Long equipmentId = 1L;
        EquipmentDto equipmentDto = new EquipmentDto();

        Equipment existingEquipment = new Equipment();
        existingEquipment.setEquipmentId(equipmentId);

        MultipartFile file = Mockito.mock(MultipartFile.class);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));

        // when, then
        assertThatThrownBy(() -> equipmentServiceImpl.updateEquipment(equipmentId, equipmentDto, MOCK_TOKEN))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "Equipment", "ID", equipmentId));

        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, never()).uploadImage(file, existingEquipment);
    }

    @Test
    @Order(8)
    void updateSomeoneElseEquipmentThrowsAccessDeniedException () {
        // given
        Long mockUserId = 123L;
        Long equipmentId = 1L;
        Long equipmentUserId = 12L;
        EquipmentDto equipmentDto = new EquipmentDto();

        Equipment existingEquipment = new Equipment();
        existingEquipment.setEquipmentId(equipmentId);
        existingEquipment.setUserId(equipmentUserId);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));
        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(existingEquipment));

        // when, then
        assertThatThrownBy(() -> equipmentServiceImpl.updateEquipment(equipmentId, equipmentDto, MOCK_TOKEN))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Ви не можете редагувати чуже обладнання.");

        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
    }

    @Test
    @Order(9)
    void uploadMainImage() {
        // given
        Long mockUserId = 123L;
        Long equipmentId = 1L;
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentId);
        equipment.setUserId(mockUserId);
        MultipartFile file = mock(MultipartFile.class);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));
        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));

        // when
        equipmentServiceImpl.uploadMainImage(equipmentId, file, MOCK_TOKEN);

        // then
        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, times(1)).uploadImage(file, equipment);
    }

    @Test
    @Order(10)
    void uploadMainImageWhenEquipmentNotExist() {
        // given
        Long mockUserId = 123L;
        Long equipmentId = 1L;
        Equipment equipment = new Equipment();
        MultipartFile file = mock(MultipartFile.class);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));

        // when, then
        assertThatThrownBy(() -> equipmentServiceImpl.uploadMainImage(equipmentId, file, MOCK_TOKEN))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "Equipment", "ID", equipmentId));

        // then
        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, never()).uploadImage(file, equipment);
    }

    @Test
    @Order(11)
    void uploadMainImageToSomeoneElseEquipmentThrowsAccessDeniedException() {
        // given
        Long mockUserId = 123L;
        Long equipmentUserId = 77L;
        Long equipmentId = 1L;
        Equipment equipment = new Equipment();
        equipment.setUserId(equipmentUserId);
        MultipartFile file = mock(MultipartFile.class);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));
        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));

        // when, then
        assertThatThrownBy(() -> equipmentServiceImpl.uploadMainImage(equipmentId, file, MOCK_TOKEN))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Ви не можете редагувати чуже обладнання.");

        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, never()).uploadImage(file, equipment);
    }

    @Test
    @Order(12)
    void uploadNullAsMainImage() {
        // given
        Long mockUserId = 123L;
        Long equipmentId = 1L;
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentId);
        equipment.setUserId(mockUserId);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));
        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));

        // when
        equipmentServiceImpl.uploadMainImage(equipmentId, null, MOCK_TOKEN);

        // then
        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, never()).uploadImage(null, equipment);
    }

    @Test
    @Order(13)
    void uploadEmptyFileAsMainImage() {
        // given
        Long mockUserId = 123L;
        Long equipmentId = 1L;
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentId);
        equipment.setUserId(mockUserId);
        MultipartFile file = mock(MultipartFile.class);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));
        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));
        when(file.isEmpty()).thenReturn(true);

        // when
        equipmentServiceImpl.uploadMainImage(equipmentId, file, MOCK_TOKEN);

        // then
        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, never()).uploadImage(file, equipment);
    }

    @Test
    @Order(14)
    void uploadMainImageWithReplaceOldOne() {
        // given
        Long mockUserId = 123L;
        Long equipmentId = 1L;
        Long imageId = 100L;
        Equipment equipment = mock(Equipment.class);
        MultipartFile file = mock(MultipartFile.class);

        Image mainImage = mock(Image.class);
        Image updatedMainImage = mock(Image.class);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));
        when(equipment.getUserId()).thenReturn(mockUserId);
        when(mainImage.getId()).thenReturn(imageId);
        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));
        when(equipment.getMainImage()).thenReturn(mainImage);

        when(imageService.uploadImage(file, equipment)).thenReturn(updatedMainImage);

        // when
        equipmentServiceImpl.uploadMainImage(equipmentId, file, MOCK_TOKEN);

        // then
        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, times(1)).deleteImage(imageId);
        verify(imageService, times(1)).uploadImage(file, equipment);
        verify(equipment, times(1)).setMainImage(updatedMainImage);
    }

    @Test
    @Order(15)
    void uploadImages() {
        // given
        Long mockUserId = 123L;
        Long equipmentId = 1L;
        Equipment equipment = spy(new Equipment());
        equipment.setEquipmentId(equipmentId);

        // Моделюємо Image для повернення
        Image image1 = new Image();
        Image image2 = new Image();

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));
        when(equipment.getUserId()).thenReturn(mockUserId);
        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));
        when(imageService.uploadImage(any(MultipartFile.class), eq(equipment)))
                .thenReturn(image1)
                .thenReturn(image2);

        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        List<MultipartFile> files = List.of(file1, file2);

        // when
        equipmentServiceImpl.uploadImages(equipmentId, files, MOCK_TOKEN);

        // then
        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository, times(2)).findEquipmentById(equipmentId);
        verify(imageService, times(files.size())).uploadImage(any(MultipartFile.class), eq(equipment));
        verify(equipment, times(files.size() - 1)).addImage(any(Image.class));
    }

    @Test
    @Order(16)
    void uploadImagesThrowsImageLimitExceededException() {
        // given
        Long equipmentId = 1L;
        Long mockUserId = 123L;
        MultipartFile file = mock(MultipartFile.class);
        Equipment equipment = mock(Equipment.class);

        List<Image> images = new ArrayList<>();
        for (int i = 0; i < MAX_IMAGE_LIMIT; i++) {
            images.add(mock(Image.class));
        }

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));
        when(equipment.getUserId()).thenReturn(mockUserId);
        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));
        when(equipment.getImages()).thenReturn(images);

        // when, then
        assertThatThrownBy(() -> equipmentServiceImpl.uploadImages(equipmentId, List.of(file), MOCK_TOKEN))
                .isInstanceOf(ImageLimitExceededException.class)
                .hasMessageContaining("Досягнуто загальний ліміт");

        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(imageService, never()).uploadImage(file, equipment);
    }

    @Test
    @Order(17)
    void addImagesToEquipmentThrowsAccessDeniedException() {
        // given
        Long mockUserId = 123L;
        Long equipmentUserId = 77L; // інший користувач
        Long equipmentId = 1L;
        Equipment equipment = new Equipment();
        equipment.setUserId(equipmentUserId);

        MultipartFile mockFile = mock(MultipartFile.class);
        List<MultipartFile> files = List.of(mockFile);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(mockUserId));
        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));

        // when, then
        assertThatThrownBy(() -> equipmentServiceImpl.uploadImages(equipmentId, files, MOCK_TOKEN))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Ви не можете редагувати чуже обладнання.");

        verify(usersFeignClient, times(1)).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository, times(1)).findEquipmentById(equipmentId);
        verify(imageService, never()).uploadImage(any(), any());
    }

    @Test
    @Order(18)
    void downloadImage() {
        // given
        Long imageId = 11L;

        // when
        equipmentServiceImpl.downloadImage(imageId);

        // then
        verify(imageService, times(1)).downloadImage(imageId);
    }

    @Test
    @Order(19)
    void getOwnerIdByEquipmentId_returnsOwnerId() {
        // given
        Long equipmentId = 1L;
        Long expectedOwnerId = 42L;

        when(equipmentRepository.findOwnerIdByEquipmentId(equipmentId))
                .thenReturn(Optional.of(expectedOwnerId));

        // when
        Long result = equipmentServiceImpl.getOwnerIdByEquipmentId(equipmentId);

        // then
        assertEquals(expectedOwnerId, result);
        verify(equipmentRepository, times(1)).findOwnerIdByEquipmentId(equipmentId);
    }

    @Test
    @Order(20)
    void getOwnerIdByEquipmentId_throwsResourceNotFoundException_ifNotFound() {
        // given
        Long equipmentId = 1L;

        when(equipmentRepository.findOwnerIdByEquipmentId(equipmentId))
                .thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> equipmentServiceImpl.getOwnerIdByEquipmentId(equipmentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Equipment not found with the given input data ID: '" + equipmentId + "'");

        verify(equipmentRepository, times(1)).findOwnerIdByEquipmentId(equipmentId);
    }

    @Test
    @Order(21)
    void deactivateEquipmentById_setsStatusToInactive_ifAuthorized() {
        // given
        Long equipmentId = 1L;
        Long ownerId = 123L;

        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentId);
        equipment.setStatus(EquipmentStatus.AVAILABLE);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(ownerId));
        when(equipmentRepository.findOwnerIdByEquipmentId(equipmentId)).thenReturn(Optional.of(ownerId));
        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));

        // when
        equipmentServiceImpl.deactivateEquipmentById(equipmentId, MOCK_TOKEN);

        // then
        assertEquals(EquipmentStatus.INACTIVE, equipment.getStatus());
        verify(usersFeignClient).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository).findOwnerIdByEquipmentId(equipmentId);
        verify(equipmentRepository).findEquipmentById(equipmentId);
    }

    @Test
    @Order(22)
    void activateEquipmentById_setsStatusToAvailable_ifAuthorized() {
        // given
        Long equipmentId = 1L;
        Long ownerId = 123L;

        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentId);
        equipment.setStatus(EquipmentStatus.INACTIVE);

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(ownerId));
        when(equipmentRepository.findOwnerIdByEquipmentId(equipmentId)).thenReturn(Optional.of(ownerId));
        when(equipmentRepository.findEquipmentById(equipmentId)).thenReturn(Optional.of(equipment));

        // when
        equipmentServiceImpl.activateEquipmentById(equipmentId, MOCK_TOKEN);

        // then
        assertEquals(EquipmentStatus.AVAILABLE, equipment.getStatus());
        verify(usersFeignClient).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository).findOwnerIdByEquipmentId(equipmentId);
        verify(equipmentRepository).findEquipmentById(equipmentId);
    }

    @Test
    @Order(23)
    void deactivateEquipmentById_throwsAccessDenied_ifUserNotOwner() {
        // given
        Long equipmentId = 1L;
        Long currentUserId = 100L;
        Long ownerId = 999L;

        when(usersFeignClient.getUserIdFromToken(MOCK_TOKEN)).thenReturn(ResponseEntity.ok(currentUserId));
        when(equipmentRepository.findOwnerIdByEquipmentId(equipmentId)).thenReturn(Optional.of(ownerId));

        // when, then
        assertThatThrownBy(() -> equipmentServiceImpl.deactivateEquipmentById(equipmentId, MOCK_TOKEN))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Ви не можете змінювати статус чужого обладнання.");

        verify(usersFeignClient).getUserIdFromToken(MOCK_TOKEN);
        verify(equipmentRepository).findOwnerIdByEquipmentId(equipmentId);
        verify(equipmentRepository, never()).findEquipmentById(any());
    }
}