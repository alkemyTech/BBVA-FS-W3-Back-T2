package com.bbva.wallet.exceptions;

public class TransactionFailedForSameUserException extends RuntimeException {

    public TransactionFailedForSameUserException(String message) {
        super(message);
    }
}
