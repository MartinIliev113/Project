package com.example.marketplace.model.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class MultipleFileValidator implements ConstraintValidator<MultipleFileValidation, List<MultipartFile>> {

    private List<String> contentTypes;
    private long size;

    @Override
    public void initialize(MultipleFileValidation constraintAnnotation) {
        this.size = constraintAnnotation.size();
        this.contentTypes = Arrays.asList(constraintAnnotation.contentTypes());
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        for (MultipartFile file : files) {
            String errorMsg = getErrorMsg(file);

            if (!errorMsg.isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(errorMsg)
                        .addConstraintViolation();

                return false;
            }
        }

        return true;
    }

    private String getErrorMsg(MultipartFile file) {
        if (file.isEmpty()) {
            return "File must be provided";
        }

        if (file.getSize() > size) {
            return "Exceeded file size. Max size: " + size;
        }

        if (!contentTypes.contains(file.getContentType())) {
            return "Invalid file extension. Supported files: " + String.join(", ", contentTypes);
        }

        return "";
    }
}


