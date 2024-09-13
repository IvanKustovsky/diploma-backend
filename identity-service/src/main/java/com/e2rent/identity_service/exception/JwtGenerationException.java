package com.e2rent.identity_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class JwtGenerationException extends RuntimeException {

    public JwtGenerationException(String message) {
        super(message);
    }
}
