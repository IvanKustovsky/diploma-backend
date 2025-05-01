package com.e2rent.rent_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RejectRentalRequestDto {

    @NotEmpty(message = "Rejection message cannot be null or empty")
    @Size(max = 255, message = "Rejection message must be less than 255 characters")
    private String rejectionMessage;
}
