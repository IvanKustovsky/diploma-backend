package com.e2rent.rent_service.dto;

import com.e2rent.rent_service.enums.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
//@Schema(name = "Register User", description = "Schema to hold User registration information")
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
    private RentalStatus status;
    private String rejectionMessage;
}
