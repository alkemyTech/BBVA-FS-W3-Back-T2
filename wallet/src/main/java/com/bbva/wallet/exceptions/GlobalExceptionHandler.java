package com.bbva.wallet.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String field = Objects.nonNull(ex.getBindingResult().getFieldError()) ? ex.getBindingResult().getFieldError().getField() : ex.getBindingResult().getObjectName();
        String error = Objects.nonNull(ex.getBindingResult().getFieldError()) ? ex.getBindingResult().getFieldError().getDefaultMessage() : "";
        Response<String> response = new Response<String>();
        response.addError(ErrorCodes.INVALID_VALUE);
        response.setMessage(String.format("El campo %s no es válido. %s", field, error));
        response.setData(field);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Response<String>> handleDuplicateEmailException(DuplicateEmailException ex) {
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.DUPLICATE_EMAIL);
        response.setMessage(ex.getMessage());
        response.setData(ex.getDuplicateEmail());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
