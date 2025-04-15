package com.e2rent.user_service.controller;

import com.e2rent.user_service.dto.*;
import com.e2rent.user_service.constants.UserConstants;
import com.e2rent.user_service.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "REST APIs for Users in E2Rent",
        description = "REST APIs in E2Rent to CREATE, FETCH, UPDATE AND DELETE user details"
)
@RestController
@RequestMapping(path = "/users/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {

    private final IUserService userService;

    @Operation(summary = "Create user REST API",
            description = "REST API to create new User inside E2Rent")
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
    public ResponseEntity<ResponseDto> registerUser(@Valid @RequestBody RegisterUserDto userDto) {
        userService.registerUser(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UserConstants.STATUS_201, UserConstants.MESSAGE_201));
    }

    @Operation(summary = "Fetch user REST API",
            description = "REST API to fetch User by email inside E2Rent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "HTTP Status Unauthorized"
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
    @GetMapping
    public ResponseEntity<UserDto> fetchUserByEmail(@RequestParam(name = "email") @Email String email) {
        UserDto userDto = userService.fetchUserByEmail(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto);
    }

    @Operation(summary = "Fetch user REST API",
            description = "REST API to fetch User by ID inside E2Rent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "HTTP Status Unauthorized"
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
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> fetchUserById(@PathVariable Long id) {
        UserDto userDto = userService.fetchUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Update user REST API",
            description = "REST API to update User inside E2Rent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "HTTP Status Unauthorized"
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
    @PutMapping
    public ResponseEntity<ResponseDto> updateUserDetails(@Valid @RequestBody UpdateUserDto updateUserDto,
                                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        userService.updateUser(updateUserDto, token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));
    }

    @Operation(summary = "Delete user REST API",
            description = "REST API to delete User inside E2Rent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "HTTP Status Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "HTTP Status Forbidden"
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
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<ResponseDto> deleteUserDetails(@RequestParam(name = "email") @Email String email) {
        userService.deleteUser(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));

    }

    @GetMapping("/getUserIdFromToken")
    public ResponseEntity<Long> getUserIdFromToken(@RequestHeader(HttpHeaders.AUTHORIZATION)
                                                       String authorizationToken) {
        String email = userService.extractEmailFromToken(authorizationToken);
        Long userId = userService.getUserIdByEmail(email);
        return ResponseEntity.ok(userId);
    }
}

