package com.lcwd.electronic.store.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ImageNameValidator implements ConstraintValidator<ImageNameValidate, String> {
    Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        logger.info("Image Name Validator: {}",s);
        if(s.isEmpty()){
            return false;
        }
        return true;
    }
}