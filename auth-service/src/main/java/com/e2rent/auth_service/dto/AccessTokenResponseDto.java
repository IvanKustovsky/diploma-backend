package com.e2rent.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AccessTokenResponseDto",
        description = "Schema representing the access token response from Keycloak")
public class AccessTokenResponseDto {

    @Schema(description = "Access token provided by the authentication server",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String access_token;

    @Schema(description = "Expiration time of the access token in seconds", example = "3600")
    private long expires_in;

    @Schema(description = "Type of the provided token, usually 'Bearer'", example = "Bearer")
    private String token_type;
}
