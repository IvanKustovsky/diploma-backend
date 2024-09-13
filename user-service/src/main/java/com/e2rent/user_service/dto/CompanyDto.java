package com.e2rent.user_service.dto;

import com.e2rent.user_service.annotation.ValidCompanyCode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "Company", description = "Schema to hold Company information")
@AllArgsConstructor @NoArgsConstructor
public class CompanyDto {

    @Schema(description = "Name of the company", example = "Tech Solutions")
    @NotEmpty(message = "Name cannot be null or empty")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Schema(description = "Code of the company", example = "11223344")
    @NotEmpty(message = "Code cannot be null or empty")
    @ValidCompanyCode
    private String code;

    @Schema(description = "Address of the company", example = "123 Main St, Washington")
    @NotEmpty(message = "Address cannot be null or empty")
    @Size(max = 255, message = "Address must be less than 255 characters")
    private String address;
}

