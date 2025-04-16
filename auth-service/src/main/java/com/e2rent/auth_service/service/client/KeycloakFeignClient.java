package com.e2rent.auth_service.service.client;

import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "keycloak", url = "${keycloak.url}")
public interface KeycloakFeignClient {

    @PostMapping(value = "/realms/{realm}/protocol/openid-connect/token",
            consumes = "application/x-www-form-urlencoded")
    AccessTokenResponse getToken(@PathVariable("realm") String realm, @RequestBody Map<String, ?> form);

    @PostMapping(value = "/realms/{realm}/protocol/openid-connect/token",
            consumes = "application/x-www-form-urlencoded")
    AccessTokenResponse refreshToken(@PathVariable("realm") String realm, @RequestBody Map<String, ?> form);

    @PostMapping(value = "/realms/{realm}/protocol/openid-connect/logout",
            consumes = "application/x-www-form-urlencoded")
    ResponseEntity<Void> logout(@PathVariable("realm") String realm, @RequestBody Map<String, ?> form);

    @PostMapping(value = "/admin/realms/{realm}/users")
    ResponseEntity<Void> createUser(@PathVariable("realm") String realm,
                                    @RequestBody UserRepresentation user,
                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);

    @GetMapping(value = "/admin/realms/{realm}/users", produces = "application/json")
    List<UserRepresentation> findUsersByEmail(@PathVariable("realm") String realm,
                                              @RequestParam("email") String email,
                                              @RequestParam("exact") boolean exact,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    );

    @DeleteMapping(value = "/admin/realms/{realm}/users/{id}")
    void deleteUser(@PathVariable("realm") String realm,
                    @PathVariable("id") String userId,
                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);
}
