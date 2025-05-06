package com.e2rent.rent_service.service.client;

import com.e2rent.rent_service.dto.EquipmentResponseDto;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "equipments", path = "/equipments/api/v1",
        fallbackFactory = EquipmentFallbackFactory.class)
public interface EquipmentFeignClient {

    @GetMapping("/{id}")
    ResponseEntity<EquipmentResponseDto> fetchEquipment(
            @PathVariable(value = "id")
            @Positive(message = "Equipment id must be positive number") Long id);
}
