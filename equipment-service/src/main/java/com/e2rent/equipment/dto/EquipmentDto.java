package com.e2rent.equipment.dto;

import com.e2rent.equipment.enums.EquipmentCondition;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Schema(name = "Equipment", description = "Schema to hold Equipment information")
@AllArgsConstructor @NoArgsConstructor
public class EquipmentDto {

    @Schema(description = "Name of the equipment", example = "Diesel generator")
    @NotEmpty(message = "Name cannot be null or empty")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Schema(description = "Description of the equipment",
            example = "Machine produces electricity by burning diesel fuel")
    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @Schema(description = "Category of the equipment", example = "Tools")
    @NotEmpty(message = "Category cannot be null or empty")
    @Size(max = 255, message = "Category must be less than 255 characters")
    private String category; // TODO: Maybe change to enum

    @Schema(description = "Price of the equipment", example = "150.00")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @Schema(description = "Condition of the equipment", example = "NEW, USED or REFURBISHED")
    @NotNull(message = "Condition cannot be null")
    @Enumerated(EnumType.STRING)
    private EquipmentCondition condition;

    @Schema(description = "User ID of the owner", example = "1")
    @Positive(message = "User ID must be greater than zero")
    private Long userId;

    @Schema(description = "Main image URL of the equipment", example = "https://example.com/main_image.jpg")
    private String mainImageUrl;

    @Schema(description = "List of image URLs of the equipment")
    private List<String> imageUrls = new ArrayList<>();
}

