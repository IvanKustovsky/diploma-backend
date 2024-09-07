package com.example.gatewayservice.service.client;

import com.example.gatewayservice.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "identity-service", path = "/auth/api/v1",
        fallbackFactory = IdentityFallbackFactory.class)
public interface IdentityFeignClient {

    @GetMapping("/validate")
    ResponseDto validateToken(@RequestParam(name = "token") String token);
}
