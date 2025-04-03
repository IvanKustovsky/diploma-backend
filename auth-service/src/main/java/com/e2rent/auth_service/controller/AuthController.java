package com.e2rent.auth_service.controller;


import com.e2rent.auth_service.dto.AccessTokenResponseDto;
import com.e2rent.auth_service.dto.LoginDto;
import com.e2rent.auth_service.dto.RegisterUserDto;
import com.e2rent.auth_service.service.IAuthService;
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

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto) {
        boolean isCreated = authService.createUser(registerUserDto);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteByEmail(@RequestParam String email) {
        try {
            authService.deleteByEmail(email);
            return ResponseEntity.ok("User successfully deleted.");
        } catch (Exception e) {  // TODO Remove thy-catch blocks from controller
            log.error("Failed to delete user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponseDto> login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
        try {
            var tokenResponse = authService.getAccessToken(loginDto, response);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponseDto> refresh(@CookieValue("refresh_token") String refreshToken, HttpServletResponse response) {
        try {
            // Якщо refreshToken не знайдений або його немає
            if (refreshToken == null) {
                log.warn("Refresh token not found.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // Викликаємо сервіс для оновлення токена
            var tokenResponse = authService.refreshToken(refreshToken, response);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
