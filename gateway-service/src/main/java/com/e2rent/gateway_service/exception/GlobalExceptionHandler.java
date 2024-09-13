package com.e2rent.gateway_service.exception;

import com.e2rent.gateway_service.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(
            Exception exception, ServerWebExchange exchange) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                exchange.getRequest().getPath().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponseDTO);
    }

    @ExceptionHandler(MissingAuthorizationHeaderException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingAuthorizationHeaderException(
            MissingAuthorizationHeaderException exception, ServerWebExchange exchange) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                exchange.getRequest().getPath().toString(),
                HttpStatus.NETWORK_AUTHENTICATION_REQUIRED,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
                .body(errorResponseDTO);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponseDto> handleServiceUnavailableException(
            ServiceUnavailableException ex, ServerWebExchange exchange) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                exchange.getRequest().getPath().toString(),
                HttpStatus.SERVICE_UNAVAILABLE,
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(errorResponseDTO);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedException(
            UnauthorizedException ex, ServerWebExchange exchange) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                exchange.getRequest().getPath().toString(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errorResponseDTO);
    }

}



