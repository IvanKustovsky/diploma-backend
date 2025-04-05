package com.e2rent.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "LoginDto", description = "Schema to hold user credentials information")
public class LoginDto {

    @NotEmpty(message = "Username cannot be null or empty")
    @Email(message = "Username should be valid")
    @Schema(description = "Username (email) of the user", example = "john.doe@example.com")
    private String username;

    @NotEmpty(message = "Password cannot be null or empty")
    @Size(min = 5, message = "Password must be at least 5 symbols")
    @Schema(description = "Password of the user", example = "password123")
    private String password;
}
