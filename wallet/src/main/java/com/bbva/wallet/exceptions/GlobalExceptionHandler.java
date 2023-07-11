package com.bbva.wallet.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
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

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Response<String>> handleAuthenticationException(AuthenticationException ex) {
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.FAILED_AUTHENTICATION);
        response.setMessage(ex.getMessage());
        response.setData("error de autenticación");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InexistentAccountException.class)
    public ResponseEntity<Response<String>> handleAccountNotFoundException(InexistentAccountException ex) {
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.ACCOUNT_NOT_FOUND);
        response.setMessage(ex.getMessage());
        response.setData(String.valueOf(ex.getAccountId()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(NoUserAccountsException.class)
    public ResponseEntity<Response<String>> handleAccountNotFoundException(NoUserAccountsException ex) {
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.ACCOUNT_NOT_FOUND);
        response.setMessage(ex.getMessage());
        response.setData("sin cuentas");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserAccountMismatchException.class)
    public ResponseEntity<Response<String>> handleUserAccountMismatchException(UserAccountMismatchException ex) {
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.INVALID_VALUE);
        response.setMessage(ex.getMessage());
        response.setData("id: " + ex.getAccountId());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

}
