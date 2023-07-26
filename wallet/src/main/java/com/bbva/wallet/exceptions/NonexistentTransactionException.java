package com.bbva.wallet.exceptions;

public class NonexistentTransactionException extends RuntimeException {
    private final Long transactionId;

    public NonexistentTransactionException(String message, Long transactionId) {
        super(message);
        this.transactionId = transactionId;
    }

    public Long getTransactionId() {
        return transactionId;
    }
}