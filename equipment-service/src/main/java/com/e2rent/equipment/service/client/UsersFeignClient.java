package com.e2rent.equipment.service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "users", path = "/users/api/v1")
public interface UsersFeignClient {

    @GetMapping("/getUserIdFromToken")
    ResponseEntity<Long> getUserIdFromToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken);
}
