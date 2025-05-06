package com.e2rent.equipment.service.impl;

import com.e2rent.equipment.dto.AdvertisementDto;
import com.e2rent.equipment.entity.Advertisement;
import com.e2rent.equipment.entity.Equipment;
import com.e2rent.equipment.enums.AdvertisementStatus;
import com.e2rent.equipment.exception.ResourceNotFoundException;
import com.e2rent.equipment.repository.AdvertisementRepository;
import com.e2rent.equipment.service.IAdvertisementService;
import com.e2rent.equipment.service.client.UsersFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdvertisementServiceImpl implements IAdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UsersFeignClient usersFeignClient;

    @Override
    @Transactional
    public void createAdvertisement(Equipment equipment) {
        Advertisement ad = Advertisement.builder()
                .equipment(equipment)
                .status(AdvertisementStatus.CREATED)
                .build();
        advertisementRepository.save(ad);
    }

    @Override
    @Transactional
    public void approveAdvertisement(Long advertisementId, String adminMessage) {
        Advertisement ad = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Advertisement", "id", String.valueOf(advertisementId)));

        ad.setStatus(AdvertisementStatus.APPROVED);
        ad.setAdminMessage(adminMessage);
    }

    @Override
    @Transactional
    public void rejectAdvertisement(Long advertisementId, String adminMessage) {
        Advertisement ad = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Advertisement", "id", String.valueOf(advertisementId)));

        ad.setStatus(AdvertisementStatus.REJECTED);
        ad.setAdminMessage(adminMessage);
    }

    @Override
    @Transactional
    public void markAsUpdated(Long equipmentId) {
        Advertisement advertisement = advertisementRepository.findByEquipment_EquipmentId(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement", "equipmentId", String.valueOf(equipmentId)));

        advertisement.setStatus(AdvertisementStatus.UPDATED);
    }

    @Override
    public Page<AdvertisementDto> getAllApproved(String authToken, Pageable pageable) {
        if (authToken != null) {
            var currentUserId = usersFeignClient.getUserIdFromToken(authToken).getBody();
            return advertisementRepository.findAllByStatusExcludingUser(
                    AdvertisementStatus.APPROVED, currentUserId, pageable);
        }
        return advertisementRepository.findAllByStatus(AdvertisementStatus.APPROVED, pageable);
    }

    @Override
    public Page<AdvertisementDto> getAllPending(Pageable pageable) {
        return advertisementRepository.findAllByStatusIn(
                List.of(AdvertisementStatus.CREATED, AdvertisementStatus.UPDATED), pageable
        );
    }

    @Override
    public Page<AdvertisementDto> getAllApprovedByUserId(Long userId, Pageable pageable) {
        return advertisementRepository.findAllApprovedByUserId(userId, AdvertisementStatus.APPROVED, pageable);
    }

    @Override
    public Page<AdvertisementDto> getMyAdvertisements(String authToken, Pageable pageable) {
        var currentUserId = usersFeignClient.getUserIdFromToken(authToken).getBody();
        return advertisementRepository.findAllByUserId(currentUserId, pageable);
    }

}