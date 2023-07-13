package com.bbva.wallet.exceptions;

public class ProhibitedAccessToTransactionsException extends RuntimeException{
    public ProhibitedAccessToTransactionsException(String message) {
        super(message);
    }
}
