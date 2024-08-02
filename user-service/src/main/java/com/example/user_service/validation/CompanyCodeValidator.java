package com.example.user_service.validation;

import com.example.user_service.annotation.ValidCompanyCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompanyCodeValidator implements ConstraintValidator<ValidCompanyCode, String> {

    private static final String COMPANY_CODE_PATTERN = "\\d{8}";

    @Override
    public void initialize(ValidCompanyCode constraintAnnotation) {
    }

    @Override
    public boolean isValid(String companyCode, ConstraintValidatorContext context) {
        if (companyCode == null) {
            return false;
        }
        return companyCode.matches(COMPANY_CODE_PATTERN);
    }
}
