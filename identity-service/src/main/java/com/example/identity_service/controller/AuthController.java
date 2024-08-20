package com.example.identity_service.controller;

import com.example.identity_service.constants.AuthConstants;
import com.example.identity_service.model.AuthRequest;
import com.example.identity_service.dto.ResponseDto;
import com.example.identity_service.dto.TokenResponseDto;
import com.example.identity_service.service.IAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "REST APIs for Authentication in E2Rent",
        description = "REST APIs in E2Rent to Authenticate user"
)
@RestController
@RequestMapping(path = "/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final IAuthService service;

    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> getToken(@Valid @RequestBody AuthRequest authRequest) {
        String token = service.generateToken(authRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new TokenResponseDto(token,
                        new ResponseDto(AuthConstants.STATUS_200, AuthConstants.MESSAGE_200)));
    }

    @GetMapping("/validate")
    public ResponseEntity<ResponseDto> validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AuthConstants.STATUS_200, AuthConstants.MESSAGE_200));
    }
}
