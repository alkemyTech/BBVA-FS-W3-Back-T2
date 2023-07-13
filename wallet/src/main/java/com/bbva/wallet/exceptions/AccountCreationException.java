package com.bbva.wallet.exceptions;

public class AccountCreationException extends RuntimeException {
    public AccountCreationException(String message) {
        super(message);
    }
}