package com.example.identity_service.controller;

import com.example.identity_service.constants.AuthConstants;
import com.example.identity_service.dto.ErrorResponseDto;
import com.example.identity_service.model.AuthRequest;
import com.example.identity_service.dto.ResponseDto;
import com.example.identity_service.dto.TokenResponseDto;
import com.example.identity_service.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
@RequestMapping(path = "/auth/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final IAuthService service;

    @Operation(summary = "Generate token REST API",
            description = "REST API to generate new JWT token inside E2Rent")
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
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "HTTP Status Service Unavailable",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> getToken(@Valid @RequestBody AuthRequest authRequest) {
        String token = service.generateToken(authRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new TokenResponseDto(token,
                        new ResponseDto(AuthConstants.STATUS_200, AuthConstants.MESSAGE_200)));
    }

    @Operation(summary = "Validate token REST API",
            description = "REST API to validate JWT token inside E2Rent")
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
    @GetMapping("/validate")
    public ResponseEntity<ResponseDto> validateToken(@RequestParam(name = "token") String token) {
        service.validateToken(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AuthConstants.STATUS_200, AuthConstants.MESSAGE_200));
    }

    @Operation(summary = "Extract email REST API",
            description = "REST API to extract email from JWT token inside E2Rent")
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
    @GetMapping("/email")
    public ResponseEntity<String> extractEmail(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String email = service.extractEmail(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(email);
    }
}
