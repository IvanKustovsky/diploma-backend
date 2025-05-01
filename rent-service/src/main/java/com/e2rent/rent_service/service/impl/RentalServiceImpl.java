package com.e2rent.rent_service.service.impl;

import com.e2rent.rent_service.dto.CreateRentalRequestDto;
import com.e2rent.rent_service.dto.RentalResponseDto;
import com.e2rent.rent_service.entity.Rental;
import com.e2rent.rent_service.enums.RentalStatus;
import com.e2rent.rent_service.exception.ResourceNotFoundException;
import com.e2rent.rent_service.mapper.RentalMapper;
import com.e2rent.rent_service.repository.RentalRepository;
import com.e2rent.rent_service.service.IRentalService;
import com.e2rent.rent_service.service.client.EquipmentFeignClient;
import com.e2rent.rent_service.service.client.UsersFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements IRentalService {

    private final RentalRepository rentalRepository;
    private final UsersFeignClient usersFeignClient;
    private final EquipmentFeignClient equipmentFeignClient;

    @Override
    @Transactional
    public void createRental(CreateRentalRequestDto request, String authToken) {
        Long currentUserId = usersFeignClient.getUserIdFromToken(authToken).getBody();

        Long ownerId = equipmentFeignClient.getOwnerIdByEquipmentId(request.getEquipmentId()).getBody();

        // Перевірка, чи орендар не є власником
        if (Objects.equals(ownerId, currentUserId)) {
            throw new AccessDeniedException("Власник не може орендувати власне обладнання.");
        }

        Rental rental = RentalMapper.INSTANCE.toRental(request);
        rental.setStatus(RentalStatus.PENDING);
        rental.setRenterId(currentUserId);
        rental.setOwnerId(ownerId);

        rentalRepository.save(rental);
    }

    @Override
    public RentalResponseDto getRentalById(Long rentalId, String authToken) {
        Long currentUserId = usersFeignClient.getUserIdFromToken(authToken).getBody();

        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", "rentalId", String.valueOf(rentalId)));

        // Перевірка доступу: тільки орендар або власник можуть бачити деталі
        if (!Objects.equals(rental.getRenterId(), currentUserId) &&
                !Objects.equals(rental.getOwnerId(), currentUserId)) {
            throw new AccessDeniedException("У вас немає доступу до цієї оренди.");
        }

        return RentalMapper.INSTANCE.toDto(rental);
    }

    @Override
    public Page<RentalResponseDto> getMyOutgoingRentals(String authToken, Pageable pageable) {
        var currentUserId = usersFeignClient.getUserIdFromToken(authToken).getBody();
        return rentalRepository.findAllByRenterId(currentUserId, pageable);
    }

    @Override
    public Page<RentalResponseDto> getMyIncomingRentals(String authToken, Pageable pageable) {
        var currentUserId = usersFeignClient.getUserIdFromToken(authToken).getBody();
        return rentalRepository.findAllByOwnerId(currentUserId, pageable);
    }

    @Override
    @Transactional
    public void approveRental(Long rentalId, String authToken) {
        Long currentUserId = usersFeignClient.getUserIdFromToken(authToken).getBody();
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", "rentalId", String.valueOf(rentalId)));

        // Перевірка права власності
        if (!Objects.equals(rental.getOwnerId(), currentUserId)) {
            throw new AccessDeniedException("Тільки власник може підтвердити цю оренду.");
        }

        if (rental.getStatus() != RentalStatus.PENDING) {
            throw new IllegalStateException("Можна підтвердити тільки оренду в статусі PENDING.");
        }

        rental.setStatus(RentalStatus.APPROVED);
        rental.setOwnerResponseAt(LocalDateTime.now());
        rentalRepository.save(rental);
    }

    @Override
    @Transactional
    public void rejectRental(Long rentalId, String rejectionMessage, String authToken) {
        Long currentUserId = usersFeignClient.getUserIdFromToken(authToken).getBody();
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", "rentalId", String.valueOf(rentalId)));

        // Перевірка права власності
        if (!Objects.equals(rental.getOwnerId(), currentUserId)) {
            throw new AccessDeniedException("Тільки власник може відхилити цю оренду.");
        }

        if (rental.getStatus() != RentalStatus.PENDING) {
            throw new IllegalStateException("Можна відхилити тільки оренду в статусі PENDING.");
        }

        rental.setStatus(RentalStatus.REJECTED);
        rental.setOwnerResponseAt(LocalDateTime.now());
        rental.setOwnerMessage(rejectionMessage);
        rentalRepository.save(rental);
    }
}
