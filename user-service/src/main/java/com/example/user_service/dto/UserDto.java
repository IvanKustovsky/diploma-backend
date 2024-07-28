package com.example.user_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

//TODO: Check fields that should be here
@Data
@Schema(name = "User", description = "Schema to hold User information")
public class UserDto {

    /*@Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "Company ID associated with the user", example = "10")
    private Long companyId;*/

    @Schema(description = "Role ID associated with the user", example = "1")
    private Long roleId;

    @Schema(description = "Indicates if the user is a company", example = "false")
    private boolean isCompany;

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
    @Pattern(regexp = "(^$|[0-9]{10,20})", message = "MobileNumber must be between 10 and 20 digits")
    private String mobileNumber;

    @Schema(description = "Password of the user", example = "password123")
    @NotEmpty(message = "Password cannot be null or empty")
    private String password;
}
