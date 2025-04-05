package com.e2rent.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Register User", description = "Schema to hold User registration information")
public class RegisterUserDto {

    @NotEmpty(message = "FirstName cannot be null or empty")
    @Size(max = 255, message = "FirstName must be less than 255 characters")
    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    @NotEmpty(message = "LastName cannot be null or empty")
    @Size(max = 255, message = "LastName must be less than 255 characters")
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Email should be valid")
    @Schema(description = "Email of the user", example = "john.doe@example.com")
    private String email;

    @NotEmpty(message = "MobileNumber cannot be null or empty")
    @Pattern(regexp = "^\\+380\\d{9}$", message = "MobileNumber must start with +380 and contain 9 digits after the country code")
    @Schema(description = "Mobile number of the user", example = "+380973958378")
    private String mobileNumber;

    @NotEmpty(message = "Password cannot be null or empty")
    @Size(min = 5, message = "Password must be at least 5 symbols")
    @Schema(description = "Password of the user", example = "password123")
    private String password;
}
