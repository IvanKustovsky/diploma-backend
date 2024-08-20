package com.example.identity_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "TokenResponse", description = "Schema to hold token and response information")
public class TokenResponseDto {

    private String token;
    private ResponseDto responseDto;
}
