package com.e2rent.rent_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//@Schema(name = "Response", description = "Schema to hold successful response information")
public class ResponseDto {

    //@Schema(description = "Status code in the response")
    private String statusCode;

    //@Schema(description = "Status message in the response")
    private String statusMessage;
}
