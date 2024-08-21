package com.example.gatewayservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
public class MissingAuthorizationHeaderException extends RuntimeException {

    public MissingAuthorizationHeaderException(String message) {
        super(message);
    }
}
