package com.e2rent.rent_service.service;

import com.e2rent.rent_service.dto.CreateRentalRequestDto;
import com.e2rent.rent_service.dto.RentalResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IRentalService {

    // Створення запиту на оренду
    void createRental(CreateRentalRequestDto request, String authToken);

    RentalResponseDto getRentalById(Long rentalId, String authToken);

    // Отримати оренди, які створив користувач
    Page<RentalResponseDto> getMyOutgoingRentals(String authToken, Pageable pageable);

    // Отримати запити на обладнання, що належить користувачу
    Page<RentalResponseDto> getMyIncomingRentals(String authToken, Pageable pageable);

    // Підтвердити оренду
    void approveRental(Long rentalId, String authToken);

    // Відхилити оренду з повідомленням
    void rejectRental(Long rentalId, String rejectionMessage, String authToken);
}
