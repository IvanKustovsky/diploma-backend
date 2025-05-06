package com.e2rent.rent_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private CompanyResponseDto company;
    private String fullName;
    private String email;
    private String mobileNumber;
}
