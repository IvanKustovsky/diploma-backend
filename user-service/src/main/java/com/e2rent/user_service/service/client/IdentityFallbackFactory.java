package com.e2rent.user_service.service.client;

import com.e2rent.user_service.exception.ServiceUnavailableException;
import com.e2rent.user_service.exception.UnauthorizedException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IdentityFallbackFactory implements FallbackFactory<IdentityFeignClient> {

    @Override
    public IdentityFeignClient create(Throwable cause) {
        log.error("Calling identity fallback factory", cause);
        if (cause instanceof FeignException &&
                ((FeignException) cause).status() == HttpStatus.UNAUTHORIZED.value()) {
            throw new UnauthorizedException("Invalid or expired token provided.");
        }
        throw new ServiceUnavailableException("Identity service is currently unavailable.");
    }
}
