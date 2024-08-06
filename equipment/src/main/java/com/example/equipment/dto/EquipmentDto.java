package com.example.equipment.dto;

import com.example.equipment.enums.EquipmentCondition;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@Schema(name = "Equipment", description = "Schema to hold Equipment information")
public class EquipmentDto {
    // TODO: Add mainImage and list of images

    @Schema(description = "Name of the equipment", example = "Diesel generator")
    @NotEmpty(message = "Name cannot be null or empty")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Schema(description = "Description of the equipment",
            example = "Machine produces electricity by burning diesel fuel")
    private String description;

    @Schema(description = "Category of the equipment", example = "Tools")
    @NotEmpty(message = "Category cannot be null or empty")
    @Size(max = 255, message = "Category must be less than 255 characters")
    private String category;

    @Schema(description = "Price of the equipment", example = "150.00")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @Schema(description = "Condition of the equipment", example = "NEW")
    //TODO: Probably should add some constraints
    private EquipmentCondition condition;

    @Schema(description = "User ID of the owner", example = "1")
    @Positive(message = "User ID must be greater than zero")
    private Long userId;
}

