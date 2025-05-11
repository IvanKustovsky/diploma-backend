package com.e2rent.equipment.dto;

import com.e2rent.equipment.enums.EquipmentCategory;
import com.e2rent.equipment.enums.EquipmentCondition;
import com.e2rent.equipment.enums.EquipmentSubcategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@Schema(name = "AdvertisementFilterDto", description = "Schema to hold Advertisement filter information")
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementFilterDto {

    private EquipmentCategory category;
    private EquipmentSubcategory subcategory;
    private EquipmentCondition condition;

    @DecimalMin(value = "0.0", message = "Мінімальна ціна має бути >= 0")
    private BigDecimal minPrice;

    @DecimalMin(value = "0.0", message = "Максимальна ціна має бути >= 0")
    private BigDecimal maxPrice;
    @Size(max = 100, message = "Ключове слово не може перевищувати 100 символів")
    private String keyword;
}
