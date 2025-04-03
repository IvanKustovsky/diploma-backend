package com.e2rent.user_service.service.client;

import com.e2rent.user_service.dto.RegisterUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", path = "/auth/api/v1", fallbackFactory = AuthFallbackFactory.class)
public interface AuthFeignClient {

    @PostMapping("/register")
    ResponseEntity<String> registerUser(@RequestBody RegisterUserDto registerUserDto);

    @DeleteMapping("/delete")
    ResponseEntity<String> deleteByEmail(@RequestParam String email);
}
