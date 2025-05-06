package com.e2rent.rent_service.dto;

import com.e2rent.rent_service.enums.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalResponseDto {

    private Long id;
    private Long equipmentId;
    private Long renterId;
    private Long ownerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String address;
    private BigDecimal totalPrice;
    private RentalStatus status;
    private LocalDateTime ownerResponseAt;
    private String rejectionMessage;
}
