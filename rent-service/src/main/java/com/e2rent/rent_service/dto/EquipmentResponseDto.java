package com.e2rent.rent_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class EquipmentResponseDto {

    @Positive(message = "Equipment id must be positive number")
    private Long id;

    @NotEmpty(message = "Name cannot be null or empty")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotNull(message = "Condition cannot be null")
    private String condition;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal pricePerDay;

    @Positive(message = "User ID must be greater than zero")
    private Long userId;
}


