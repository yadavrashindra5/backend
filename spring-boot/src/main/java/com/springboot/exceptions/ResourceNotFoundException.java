package com.springboot.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException{
    private String message;
    private HttpStatus status;
    public ResourceNotFoundException(String message, HttpStatus status){
        super(message);
        this.message=message;
        this.status=status;
    }

    public String getMessage() {
        return message;
    }
    public HttpStatus getStatus() {
        return status;
    }
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return "ResourceNotFoundException{" + "message=" + message + ", status=" + status + '}';
    }
}
