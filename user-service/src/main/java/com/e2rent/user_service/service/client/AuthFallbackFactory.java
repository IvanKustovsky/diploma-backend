package com.e2rent.user_service.service.client;

import com.e2rent.user_service.dto.RegisterUserDto;
import com.e2rent.user_service.exception.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthFallbackFactory implements FallbackFactory<AuthFeignClient> {

    @Override
    public AuthFeignClient create(Throwable cause) {
        // Логування помилки, яка викликала fallback
        log.error("Error calling auth-service: {}", cause.getMessage());

        // Повертаємо fallback-реалізацію AuthFeignClient
        return new AuthFeignClient() {

            @Override
            public ResponseEntity<String> registerUser(RegisterUserDto registerUserDto) {
                log.warn("Fallback for registerUser. Returning fallback response.");
                throw new ServiceUnavailableException("Auth service unavailable: " + cause.getMessage());
            }

            @Override
            public ResponseEntity<String> deleteByEmail(String email) {
                log.warn("Fallback for deleteByEmail. Returning fallback response.");
                throw new ServiceUnavailableException("Auth service unavailable: " + cause.getMessage());
            }
        };
    }
}
