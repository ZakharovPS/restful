package com.zakharov.restful.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, HttpStatus status) {
        return new ResponseEntity<>(ex, status);
    }

    @ExceptionHandler(EmptyDBException.class)
    protected ResponseEntity<Object> handleEmptyDB(EmptyDBException ex, HttpStatus status) {
        return new ResponseEntity<>(ex, status);
    }
}
