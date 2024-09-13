package com.e2rent.equipment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ImageLimitExceededException extends RuntimeException {

    public ImageLimitExceededException(String message) {
        super(message);
    }
}
