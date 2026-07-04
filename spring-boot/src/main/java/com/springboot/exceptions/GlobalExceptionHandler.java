package com.springboot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handlerNullPointerException(NullPointerException e){
        logger.error("NullPointerException : {}", e.getMessage());
        return ResponseEntity.badRequest().body("NullPointerException");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handlerResourceNotFoundException(ResourceNotFoundException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(),false, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
}
