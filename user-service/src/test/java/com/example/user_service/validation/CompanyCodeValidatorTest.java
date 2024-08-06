package com.example.user_service.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class CompanyCodeValidatorTest {

    @InjectMocks
    private static CompanyCodeValidator validator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void setUp() {
        validator = new CompanyCodeValidator();
        // Initialize the validator (not necessary but can be done for clarity)
        validator.initialize(null);
    }

    @Test
    void isValid_ShouldReturnFalse_WhenCompanyCodeIsNull() {
        // when
        boolean result = validator.isValid(null, constraintValidatorContext);

        // then
        assertFalse(result);
    }

    @Test
    void isValid_ShouldReturnFalse_WhenCompanyCodeIsNotEightDigits() {
        // given
        String companyCode = "1234567"; // 7 digits
        String companyCode2 = "123456789"; // 9 digits
        String companyCode3 = "abcd1234"; // Not numeric

        // when
        boolean result1 = validator.isValid(companyCode, constraintValidatorContext);
        boolean result2 = validator.isValid(companyCode2, constraintValidatorContext);
        boolean result3 = validator.isValid(companyCode3, constraintValidatorContext);

        // then
        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenCompanyCodeIsEightDigits() {
        // given
        String companyCode = "12345678"; // 8 digits

        // when
        boolean result = validator.isValid(companyCode, constraintValidatorContext);

        // then
        assertTrue(result);
    }
}