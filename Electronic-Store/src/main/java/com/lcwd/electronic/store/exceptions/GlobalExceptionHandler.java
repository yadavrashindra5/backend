package com.lcwd.electronic.store.exceptions;

import com.lcwd.electronic.store.response.ApiResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> resourceNotFoundException(ResourceNotFoundException ex){
        ApiResponse<String> resourceNotFound = ApiResponse.failure("Resource Not Found",null);
        return new ResponseEntity<>(resourceNotFound, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleMethodNotValidArgumentException(MethodArgumentNotValidException ex){
        List<ObjectError> allErrors =  ex.getBindingResult().getAllErrors();

        Map<String,Object>map=new HashMap<>();

        allErrors.stream().forEach((objectError -> {
            String message=objectError.getDefaultMessage();
            String key=((FieldError)objectError).getField();
            map.put(key,message);
        }));
        ApiResponse<Map<String,Object>> apiResponse=ApiResponse.failure("Valid exception",map);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponse<String>> handleBadApiRequest(BadApiRequest ex){
        ApiResponse<String> apiResponse=ApiResponse.failure("Bad Api Request",null);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }
}
