package com.e2rent.equipment.dto;

import com.e2rent.equipment.enums.AdvertisementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@Schema(name = "Advertisement", description = "Schema to hold Advertisement information")
@NoArgsConstructor @AllArgsConstructor
public class AdvertisementDto {

    private Long id;
    private Long equipmentId;
    private String equipmentName;
    private BigDecimal price;
    private Long mainImageId;
    private AdvertisementStatus status;
    private String adminMessage;
}
