package com.e2rent.rent_service.service.client;

import com.e2rent.rent_service.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "users", path = "/users/api/v1",
        fallbackFactory = UsersFallbackFactory.class)
public interface UsersFeignClient {

    @GetMapping("/getUserIdFromToken")
    ResponseEntity<Long> getUserIdFromToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken);

    @GetMapping("/{id}")
    ResponseEntity<UserResponseDto> fetchUserById(@PathVariable("id") Long id);
}

