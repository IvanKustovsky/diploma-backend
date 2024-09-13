package com.e2rent.user_service.annotation;

import com.e2rent.user_service.validation.CompanyCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CompanyCodeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCompanyCode {
    String message() default "Code must be exactly 8 digits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
