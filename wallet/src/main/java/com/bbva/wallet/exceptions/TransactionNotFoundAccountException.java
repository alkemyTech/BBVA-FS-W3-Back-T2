package com.bbva.wallet.exceptions;


public class TransactionNotFoundAccountException extends RuntimeException {

    public TransactionNotFoundAccountException(String message) {
        super(message);
    }

}
