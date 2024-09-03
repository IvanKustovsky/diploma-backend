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
    void handleUserAlreadyExistsException() {
        // Given
        String errorMessage = "User already exists";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(errorMessage);

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler
                .handleCardAlreadyExistsException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(errorMessage, responseEntity.getBody().getErrorMessage());
    }

    @Test
    void handleCompanyAlreadyExistsException() {
        // Given
        String errorMessage = "Company already exists";
        CompanyAlreadyExistsException exception = new CompanyAlreadyExistsException(errorMessage);

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler
                .handleCompanyAlreadyExistsException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(errorMessage, responseEntity.getBody().getErrorMessage());
    }

    @Test
    void handleServiceUnavailableException() {
        // Given
        String errorMessage = "Identity service is currently unavailable.";
        ServiceUnavailableException exception = new ServiceUnavailableException(errorMessage);

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler
                .handleServiceUnavailableException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(errorMessage, responseEntity.getBody().getErrorMessage());
    }

    @Test
    void handleUnauthorizedException() {
        // Given
        String errorMessage = "Identity service is currently unavailable.";
        UnauthorizedException exception = new UnauthorizedException(errorMessage);

        // When
        ResponseEntity<ErrorResponseDto> responseEntity = globalExceptionHandler
                .handleUnauthorizedException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(errorMessage, responseEntity.getBody().getErrorMessage());
    }
}
