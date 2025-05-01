package com.e2rent.rent_service.dto;

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
public class CreateRentalRequestDto {

    private Long equipmentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String address;
}
