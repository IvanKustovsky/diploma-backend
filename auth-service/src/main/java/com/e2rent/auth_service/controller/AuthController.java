package com.e2rent.auth_service.controller;


import com.e2rent.auth_service.dto.AccessTokenResponseDto;
import com.e2rent.auth_service.dto.ErrorResponseDto;
import com.e2rent.auth_service.dto.LoginDto;
import com.e2rent.auth_service.dto.RegisterUserDto;
import com.e2rent.auth_service.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final IAuthService authService;

    @Operation(summary = "Register user REST API",
            description = "REST API to register new User inside E2Rent Keycloak realm")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status Created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "HTTP Status Conflict"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto) {
        boolean isCreated = authService.createUser(registerUserDto);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed.");
        }
    }

    @Operation(summary = "Delete user REST API",
            description = "REST API to delete User inside E2Rent Keycloak realm")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status Not Found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteByEmail(@RequestParam String email) {
        authService.deleteByEmail(email);
        return ResponseEntity.ok("User successfully deleted.");
    }

    @Operation(summary = "Login user REST API",
            description = "REST API to login User inside E2Rent Keycloak realm")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "HTTP Status Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponseDto> login(@RequestBody @Valid LoginDto loginDto,
                                                        HttpServletResponse response) {
        var tokenResponse = authService.getAccessToken(loginDto, response);
        return ResponseEntity.ok(tokenResponse);
    }


    @Operation(summary = "Refresh token REST API",
            description = "REST API to refresh access_token inside E2Rent Keycloak realm")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponseDto> refresh(@CookieValue("refresh_token") String refreshToken,
                                                          HttpServletResponse response) {
        if (refreshToken == null) {
            log.warn("Refresh token not found.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        var tokenResponse = authService.refreshToken(refreshToken, response);
        return ResponseEntity.ok(tokenResponse);
    }
}
