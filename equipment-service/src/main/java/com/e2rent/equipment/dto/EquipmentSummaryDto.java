package com.e2rent.equipment.dto;

import com.e2rent.equipment.enums.EquipmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@Schema(name = "EquipmentSummary", description = "Schema to hold Equipment Summary information")
@AllArgsConstructor @NoArgsConstructor
public class EquipmentSummaryDto {

    @Schema(description = "Equipment ID", example = "101")
    @Positive(message = "Equipment ID must be greater than zero")
    private Long id;

    @Schema(description = "Name of the equipment", example = "Diesel generator")
    @NotEmpty(message = "Name cannot be null or empty")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Schema(description = "Price of the equipment", example = "150.00")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @Schema(description = "Status of the equipment", example = "AVAILABLE")
    private EquipmentStatus status;

    @Schema(description = "Main image ID", example = "6")
    private Long mainImageId;
}
