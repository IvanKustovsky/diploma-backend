package com.e2rent.identity_service.dto;

import com.e2rent.identity_service.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@Schema(name = "User", description = "Schema to hold User information")
@AllArgsConstructor @NoArgsConstructor
public class UserDto {

    @Schema(hidden = true, description = "Role ID associated with the user", example = "1")
    private List<Role> roles;

    @Schema(description = "Email of the user", example = "john.doe@example.com")
    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "Password of the user", example = "password123")
    @NotEmpty(message = "Password cannot be null or empty")
    @Size(min = 5, message = "Password must be at least 5 symbols")
    private String password;
}
