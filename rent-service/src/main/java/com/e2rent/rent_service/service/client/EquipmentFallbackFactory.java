package com.e2rent.rent_service.service.client;

import com.e2rent.rent_service.exception.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@Slf4j
public class EquipmentFallbackFactory implements FallbackFactory<EquipmentFeignClient> {

    @Override
    public EquipmentFeignClient create(Throwable cause) {
        // Логування помилки, яка викликала fallback
        log.error("Error calling equipment-service: {}", cause.getMessage());

        // Повертаємо fallback-реалізацію EquipmentFeignClient
        return new EquipmentFeignClient() {
            @Override
            public ResponseEntity<Long> getOwnerIdByEquipmentId(@PathVariable("equipmentId") Long equipmentId) {
                log.warn("Fallback for getOwnerIdByEquipmentId. Returning fallback response.");
                throw new ServiceUnavailableException("Equipment service unavailable: " + cause.getMessage());
            }
        };
    }
}
