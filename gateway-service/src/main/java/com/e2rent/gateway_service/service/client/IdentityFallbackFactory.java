package com.e2rent.gateway_service.service.client;

import com.e2rent.gateway_service.exception.ServiceUnavailableException;
import com.e2rent.gateway_service.exception.UnauthorizedException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IdentityFallbackFactory implements FallbackFactory<IdentityFeignClient> {

    @Override // TODO Fix throwing 500 error
    public IdentityFeignClient create(Throwable cause) {
        log.warn("Calling identity fallback factory");
        if (cause instanceof FeignException &&
                ((FeignException) cause).status() == HttpStatus.UNAUTHORIZED.value()) {
            log.warn("Unauthorized request: Invalid or expired token provided.");
            throw new UnauthorizedException("Invalid or expired token provided.");
        }
        log.warn("Identity service is unavailable");
        throw new ServiceUnavailableException("Identity service is currently unavailable.");
    }
}
