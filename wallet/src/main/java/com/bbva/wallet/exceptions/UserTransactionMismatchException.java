package com.bbva.wallet.exceptions;

public class UserTransactionMismatchException extends RuntimeException {
    private final Long transactionId;

    public UserTransactionMismatchException(String message, Long transactionId) {
        super(message);
        this.transactionId = transactionId;
    }

    public Long getTransactionId() {
        return transactionId;
    }
}