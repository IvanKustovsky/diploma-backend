package com.e2rent.equipment.service;

import com.e2rent.equipment.dto.AdvertisementDto;
import com.e2rent.equipment.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAdvertisementService {

    void createAdvertisement(Equipment equipment);

    void approveAdvertisement(Long advertisementId, String adminMessage);

    void rejectAdvertisement(Long advertisementId, String adminMessage);

    void markAsUpdated(Long equipmentId);

    Page<AdvertisementDto> getAllApproved(Pageable pageable);

    Page<AdvertisementDto> getAllPending(Pageable pageable); // для модератора

    Page<AdvertisementDto> getAllApprovedByUserId(Long userId, Pageable pageable);

}
