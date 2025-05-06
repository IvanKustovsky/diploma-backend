package com.e2rent.rent_service.service.client;

import com.e2rent.rent_service.dto.UserResponseDto;
import com.e2rent.rent_service.exception.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@Slf4j
public class UsersFallbackFactory implements FallbackFactory<UsersFeignClient> {

    @Override
    public UsersFeignClient create(Throwable cause) {
        // Логування помилки, яка викликала fallback
        log.error("Error calling users-service: {}", cause.getMessage());

        // Повертаємо fallback-реалізацію UsersFeignClient
        return new UsersFeignClient() {
            @Override
            public ResponseEntity<Long> getUserIdFromToken(String authToken) {
                log.warn("Fallback for getUserIdFromToken. Returning fallback response.");
                throw new ServiceUnavailableException("Users service unavailable: " + cause.getMessage());
            }

            @Override
            public ResponseEntity<UserResponseDto> fetchUserById(@PathVariable("id") Long id) {
                log.warn("Fallback for fetchUserById. Returning fallback response.");
                throw new ServiceUnavailableException("Users service unavailable: " + cause.getMessage());
            }
        };
    }
}
