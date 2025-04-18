package com.e2rent.equipment.exception;

import com.e2rent.equipment.dto.ErrorResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private static WebRequest webRequest;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeAll
    static void setup() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        webRequest = new ServletWebRequest(request);
    }

    @Test
    void handleMethodArgumentNotValid() {
        // Given
        var fieldError = new FieldError("objectName", "field", "defaultMessage");
        var bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(fieldError);
        var methodParameter = new MethodParameter(this.getClass().getDeclaredMethods()[0], -1);
        var exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        // When
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleMethodArgumentNotValid(exception,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
        Objects.requireNonNull(responseEntity);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, String> errors = ((Map<String, String>) responseEntity.getBody());
        assertEquals("defaultMessage", errors.get("field"));
    }

    @Test
    void handleGlobalException() {
        // Given
        String errorMessage = "Global error";
        Exception exception = new Exception("Global error");

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler
                .handleGlobalException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(errorMessage, responseEntity.getBody().getErrorMessage());
    }

    @Test
    void handleResourceNotFoundException() {
        // Given
        var exception = new ResourceNotFoundException("Resource not found", "resource", "value");

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler
                .handleResourceNotFoundException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Resource not found not found with the given input data resource: 'value'",
                responseEntity.getBody().getErrorMessage());
    }

    @Test
    void handleImageProcessingException() {
        // Given
        var exception = new ImageProcessingException("Unsupported image type: " + MediaType.IMAGE_GIF);

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler
                .handleImageProcessingException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void handleImageLimitExceededException() {
        // Given
        var exception = new ImageLimitExceededException("Reached maximum limit (5) " +
                "of images per one equipment.)");

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler
                .handleImageLimitExceededException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void handleAccessDeniedException() {
        // Given
        var exception = new AccessDeniedException("Ви не можете редагувати чуже обладнання.");

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler
                .handleAccessDeniedException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
}