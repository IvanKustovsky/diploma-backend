package com.e2rent.rent_service.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "equipments", path = "/equipments/api/v1",
        fallbackFactory = EquipmentFallbackFactory.class)
public interface EquipmentFeignClient {

    @GetMapping("/{equipmentId}/owner")
    ResponseEntity<Long> getOwnerIdByEquipmentId(@PathVariable("equipmentId") Long equipmentId);
}
