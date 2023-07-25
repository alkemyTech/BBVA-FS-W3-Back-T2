package com.bbva.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

    @ExceptionHandler(NonexistentTransactionException.class)
    public ResponseEntity<Response<String>> handleNonexistentTransactionException(NonexistentTransactionException ex) {
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.TRANSACTION_NOT_FOUND);
        response.setMessage(ex.getMessage());
        response.setData("id: " + String.valueOf(ex.getTransactionId()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserTransactionMismatchException.class)
    public ResponseEntity<Response<String>> handleUserTransactionMismatchException(UserTransactionMismatchException ex) {
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.INVALID_VALUE);
        response.setMessage(ex.getMessage());
        response.setData("id: " + String.valueOf(ex.getTransactionId()));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(InsuficientBalanceException.class)
    public ResponseEntity<Response<String>> handleInsuficientBalanceException(InsuficientBalanceException ex) {
        Response<String> response = new Response<>();
        String errorMessage = ex.getMessage();
        response.addError(ErrorCodes.INSUFICIENT_BALANCE);
        response.setMessage(errorMessage);
        response.setData("Error con cuenta cbu: " + ex.getCbu());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Response<String>> handleAccountNotFoundException(AccountNotFoundException ex) {
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.ACCOUNT_NOT_FOUND);
        response.setMessage(ex.getMessage());
        response.setData(String.valueOf(ex.getCurrency()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Response<String>> handleInsufficientFundsException(InsufficientFundsException ex) {
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.INSUFFICIENT_FUNDS);
        response.setMessage(ex.getMessage());
        response.setData("account balance");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
    public ResponseEntity<Response<String>> handleNoUserAccountsException(NoUserAccountsException ex) {
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response<String>> handleAccessDeniedException(AccessDeniedException ex) {
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.ACCESS_DENIED);
        response.setMessage("Sin permisos necesarios para realizar esta acción.");
        response.setData("acceso denegado");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(DeletedUserException.class)
    public ResponseEntity<Response<String>> handleAuthenticationException(DeletedUserException ex) {
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.DELETED_USER);
        response.setMessage(ex.getMessage());
        response.setData(ex.getDeletedUser());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    @ExceptionHandler(InvalidUrlRequestException.class)
    public ResponseEntity<Response<String>> handleInvalidUrlRequestException(InvalidUrlRequestException ex) {
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.INVALID_URL_REQUEST);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response<String>> handleUserNotFoundException(UserNotFoundException ex) {
        Response<String> response = new Response<>();
        response.addError(ErrorCodes.INVALID_VALUE);
        response.setMessage(ex.getMessage());
        response.setData("id: " + String.valueOf(ex.getId()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}

