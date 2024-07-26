package com.example.user_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

//TODO: Check fields that should be here
@Data
@Schema(name = "Company", description = "Schema to hold Company information")
public class CompanyDto {

    @Schema(description = "Company ID", example = "1")
    private Long id;

    @Schema(description = "Name of the company", example = "Tech Solutions")
    @NotEmpty(message = "Name cannot be null or empty")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Schema(description = "Code of the company", example = "TS123")
    @NotEmpty(message = "Code cannot be null or empty")
    @Size(max = 20, message = "Code must be less than 20 characters")
    private String code;

    @Schema(description = "Contact number of the company", example = "+1234567890")
    @NotEmpty(message = "ContactNumber cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10,20})", message = "ContactNumber must be between 10 and 20 digits")
    private String contactNumber;

    @Schema(description = "Address of the company", example = "123 Main St, Cityville")
    @NotEmpty(message = "Address cannot be null or empty")
    @Size(max = 255, message = "Address must be less than 255 characters")
    private String address;
}

