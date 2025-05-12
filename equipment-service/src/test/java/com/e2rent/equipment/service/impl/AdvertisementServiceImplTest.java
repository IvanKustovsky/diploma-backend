package com.e2rent.equipment.service.impl;

import com.e2rent.equipment.dto.AdvertisementDto;
import com.e2rent.equipment.dto.AdvertisementFilterDto;
import com.e2rent.equipment.entity.Advertisement;
import com.e2rent.equipment.entity.Equipment;
import com.e2rent.equipment.enums.AdvertisementStatus;
import com.e2rent.equipment.enums.EquipmentStatus;
import com.e2rent.equipment.exception.ResourceNotFoundException;
import com.e2rent.equipment.repository.AdvertisementRepository;
import com.e2rent.equipment.service.client.UsersFeignClient;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Advertisement Service Test Class")
class AdvertisementServiceImplTest {

    @Mock
    private AdvertisementRepository advertisementRepository;

    @Mock
    private UsersFeignClient usersFeignClient;

    @InjectMocks
    private AdvertisementServiceImpl advertisementService;

    @Test
    @Order(1)
    void createAdvertisement() {
        // given
        Equipment equipment = new Equipment();
        Advertisement expectedAd = Advertisement.builder()
                .equipment(equipment)
                .status(AdvertisementStatus.CREATED)
                .build();

        when(advertisementRepository.save(any(Advertisement.class))).thenReturn(expectedAd);

        // when
        advertisementService.createAdvertisement(equipment);

        // then
        verify(advertisementRepository, times(1)).save(any(Advertisement.class));
    }

    @Test
    @Order(2)
    void approveAdvertisement() {
        // given
        Long adId = 1L;
        String message = "Approved by admin";
        Advertisement ad = new Advertisement();
        when(advertisementRepository.findById(adId)).thenReturn(Optional.of(ad));

        // when
        advertisementService.approveAdvertisement(adId, message);

        // then
        assertEquals(AdvertisementStatus.APPROVED, ad.getStatus());
        assertEquals(message, ad.getAdminMessage());
    }

    @Test
    @Order(3)
    void approveAdvertisementThrowsResourceNotFoundException() {
        // given
        Long adId = 1L;
        String message = "Approved by admin";

        when(advertisementRepository.findById(adId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> advertisementService.approveAdvertisement(adId, message))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "Advertisement", "id", adId));

        verify(advertisementRepository, times(1)).findById(adId);
    }

    @Test
    @Order(4)
    void rejectAdvertisement() {
        // given
        Long adId = 2L;
        String message = "Rejected by admin";
        Advertisement ad = new Advertisement();
        when(advertisementRepository.findById(adId)).thenReturn(Optional.of(ad));

        // when
        advertisementService.rejectAdvertisement(adId, message);

        // then
        assertEquals(AdvertisementStatus.REJECTED, ad.getStatus());
        assertEquals(message, ad.getAdminMessage());
    }

    @Test
    @Order(5)
    void rejectAdvertisementThrowsResourceNotFoundException() {
        // given
        Long adId = 2L;
        String message = "Rejected by admin";

        when(advertisementRepository.findById(adId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> advertisementService.rejectAdvertisement(adId, message))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "Advertisement", "id", adId));

        verify(advertisementRepository, times(1)).findById(adId);
    }

    @Test
    @Order(6)
    void markAsUpdated() {
        // given
        Long equipmentId = 3L;
        Advertisement ad = new Advertisement();
        when(advertisementRepository.findByEquipment_EquipmentId(equipmentId)).thenReturn(Optional.of(ad));

        // when
        advertisementService.markAsUpdated(equipmentId);

        // then
        assertEquals(AdvertisementStatus.UPDATED, ad.getStatus());
    }

    @Test
    @Order(7)
    void getAllApproved_shouldExcludeCurrentUserAdvertisements() {
        // given
        String authToken = "Bearer someValidToken";
        Long currentUserId = 42L;
        Pageable pageable = PageRequest.of(0, 10);

        List<AdvertisementDto> ads = List.of(
                new AdvertisementDto(1L, 101L, "Equipment 1", EquipmentStatus.AVAILABLE, BigDecimal.valueOf(100), 1001L, AdvertisementStatus.APPROVED, null),
                new AdvertisementDto(2L, 102L, "Equipment 2", EquipmentStatus.AVAILABLE, BigDecimal.valueOf(200), 1002L, AdvertisementStatus.APPROVED, null)
        );
        Page<AdvertisementDto> expectedPage = new PageImpl<>(ads, pageable, ads.size());

        when(usersFeignClient.getUserIdFromToken(authToken)).thenReturn(ResponseEntity.ok(currentUserId));
        when(advertisementRepository.findFilteredAdvertisements(null, null, null,
                BigDecimal.valueOf(50L), null, "keyword", currentUserId, pageable))
                .thenReturn(expectedPage);

        // when
        Page<AdvertisementDto> result = advertisementService.searchFilteredAdvertisements(authToken,
                new AdvertisementFilterDto(null, null, null,
                        BigDecimal.valueOf(50L), null, "keyword"), pageable);

        // then
        assertEquals(expectedPage, result);
        verify(usersFeignClient).getUserIdFromToken(authToken);
        verify(advertisementRepository).findFilteredAdvertisements(null, null, null,
                BigDecimal.valueOf(50L), null, "keyword", currentUserId, pageable);
    }

    @Test
    @Order(8)
    void getAllPending() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<AdvertisementDto> ads = List.of(new AdvertisementDto(/* ... */), new AdvertisementDto(/* ... */));
        Page<AdvertisementDto> page = new PageImpl<>(ads, pageable, ads.size());
        when(advertisementRepository.findAllByStatusIn(List.of(AdvertisementStatus.CREATED, AdvertisementStatus.UPDATED), pageable)).thenReturn(page);

        // when
        Page<AdvertisementDto> result = advertisementService.getAllPending(pageable);

        // then
        assertEquals(page, result);
    }

    @Test
    @Order(9)
    void getAllApprovedByUserId() {
        // given
        Long userId = 123L;
        Pageable pageable = PageRequest.of(0, 10);
        List<AdvertisementDto> ads = List.of(new AdvertisementDto(/* ... */), new AdvertisementDto(/* ... */));
        Page<AdvertisementDto> page = new PageImpl<>(ads, pageable, ads.size());

        when(advertisementRepository.findAllApprovedByUserId(userId, AdvertisementStatus.APPROVED, pageable)).thenReturn(page);

        // when
        Page<AdvertisementDto> result = advertisementService.getAllApprovedByUserId(userId, pageable);

        // then
        assertEquals(page, result);
    }

    @Test
    @Order(10)
    void getMyAdvertisements() {
        // given
        String authToken = "Bearer someValidToken";
        Long currentUserId = 42L;
        Pageable pageable = PageRequest.of(0, 10);
        List<AdvertisementDto> ads = List.of(new AdvertisementDto(/* ... */), new AdvertisementDto(/* ... */));
        Page<AdvertisementDto> page = new PageImpl<>(ads, pageable, ads.size());

        when(usersFeignClient.getUserIdFromToken(authToken)).thenReturn(ResponseEntity.ok(currentUserId));
        when(advertisementRepository.findAllByUserId(currentUserId, pageable)).thenReturn(page);

        // when
        Page<AdvertisementDto> result = advertisementService.getMyAdvertisements(authToken, pageable);

        // then
        assertEquals(page, result);
    }
}