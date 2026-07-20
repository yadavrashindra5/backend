package com.lcwd.electronic.store.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValidate {
    String message() default "Invalid Image Name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
