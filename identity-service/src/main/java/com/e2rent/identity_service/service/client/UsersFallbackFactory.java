package com.e2rent.identity_service.service.client;

import com.e2rent.identity_service.exception.ServiceUnavailableException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UsersFallbackFactory implements FallbackFactory<UsersFeignClient> {

    @Override
    public UsersFeignClient create(Throwable cause) {
        return email -> {
            log.error("Calling fallback method for email: {}", email);
            if (cause instanceof FeignException
                    && ((FeignException) cause).status() == HttpStatus.NOT_FOUND.value()) {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
            throw new ServiceUnavailableException("User service is currently unavailable.");
        };
    }
}
