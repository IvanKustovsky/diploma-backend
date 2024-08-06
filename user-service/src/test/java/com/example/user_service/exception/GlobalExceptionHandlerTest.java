package com.example.user_service.exception;

import com.example.user_service.dto.ErrorResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private static WebRequest webRequest;

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
        Exception exception = new Exception("Global error");

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler.handleGlobalException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Global error", responseEntity.getBody().getErrorMessage());
    }

    @Test
    void handleResourceNotFoundException() {
        // Given
        var exception = new ResourceNotFoundException("Resource not found", "resource", "value");

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler.handleResourceNotFoundException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Resource not found not found with the given input data resource: 'value'",
                responseEntity.getBody().getErrorMessage());
    }

    @Test
    void handleUserAlreadyExistsException() {
        // Given
        UserAlreadyExistsException exception = new UserAlreadyExistsException("User already exists");

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler.handleCardAlreadyExistsException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("User already exists", responseEntity.getBody().getErrorMessage());
    }

    @Test
    void handleAuthenticationException() {
        // Given
        AuthenticationException exception = new AuthenticationException("Authentication failed") {
        };

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler.handleAuthenticationException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Invalid email or password", responseEntity.getBody().getErrorMessage());
    }

    @Test
    void handleCompanyAlreadyExistsException() {
        // Given
        CompanyAlreadyExistsException exception = new CompanyAlreadyExistsException("Company already exists");

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler.handleCompanyAlreadyExistsException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Company already exists", responseEntity.getBody().getErrorMessage());
    }
}
