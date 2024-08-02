package com.example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {


    @Schema(description = "Email of the user", example = "john.doe@example.com")
    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "Password of the user", example = "password123")
    @NotEmpty(message = "Password cannot be null or empty")
    @Size(min = 5, message = "Password must be at least 5 symbols")
    private String password;
    }

