package com.e2rent.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Schema(name = "User", description = "Schema to hold User information")
@AllArgsConstructor @NoArgsConstructor
public class UserDto {

    @Schema(description = "Company of the user if exists", example = "Facebook")
    private CompanyDto company;

    @Schema(description = "Full name of the user", example = "John Doe")
    @NotEmpty(message = "FullName cannot be null or empty")
    @Size(max = 255, message = "FullName must be less than 255 characters")
    private String fullName;

    @Schema(description = "Email of the user", example = "john.doe@example.com")
    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "Mobile number of the user", example = "+380973958378")
    @NotEmpty(message = "MobileNumber cannot be null or empty")
    @Pattern(regexp = "^\\+380\\d{9}$", message = "MobileNumber must start with +380 and contain 9 digits after the country code")
    private String mobileNumber;
}
