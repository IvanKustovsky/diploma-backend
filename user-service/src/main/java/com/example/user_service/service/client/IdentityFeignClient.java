package com.example.user_service.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "identity-service", path = "/auth/api/v1",
        fallbackFactory = IdentityFallbackFactory.class)
public interface IdentityFeignClient {

    @GetMapping("/email")
    ResponseEntity<String> extractEmail(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
