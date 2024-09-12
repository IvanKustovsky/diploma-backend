package com.example.equipment.exception;

import com.example.equipment.dto.ErrorResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

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
        FieldError fieldError = new FieldError("objectName", "field", "defaultMessage");
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(fieldError);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // When
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleMethodArgumentNotValid(exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

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
    void handleImageUploadException() {
        // Given
        var exception = new ImageUploadException("Unsupported image type: " + MediaType.IMAGE_GIF);

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler
                .handleImageUploadException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, responseEntity.getStatusCode());
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
}