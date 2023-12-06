package com.example.marketplace.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
public @interface FileValidation {
    long size() default 5 * 1024 * 1024;

    String[] contentTypes();

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
