package com.e2rent.equipment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdvertisementModerationRequestDto {

    @NotBlank(message = "Admin message is required")
    private String adminMessage;
}
