package com.example.identity_service.service.client;

import com.example.identity_service.dto.UserDto;
import jakarta.validation.constraints.Email;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users", fallbackFactory = UsersFallbackFactory.class, path = "/api/v1")
public interface UsersFeignClient {

    @GetMapping
    ResponseEntity<UserDto> fetchUser(@RequestParam @Email String email);
}
