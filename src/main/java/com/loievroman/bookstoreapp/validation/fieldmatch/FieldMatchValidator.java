package com.loievroman.bookstoreapp.validation.fieldmatch;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field firstField = value.getClass().getDeclaredField(firstFieldName);
            Field secondField = value.getClass().getDeclaredField(secondFieldName);
            firstField.setAccessible(true);
            secondField.setAccessible(true);

            Object firstValue = firstField.get(value);
            Object secondValue = secondField.get(value);

            if (firstValue == null || secondValue == null) {
                return false;
            }
            return firstValue.equals(secondValue);
        } catch (Exception e) {
            return false;
        }
    }
}
