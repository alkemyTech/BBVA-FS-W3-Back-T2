package com.bbva.wallet.exceptions;

public class NoUserAccountsException extends RuntimeException {
    public NoUserAccountsException(String message) {
        super(message);
    }
}
