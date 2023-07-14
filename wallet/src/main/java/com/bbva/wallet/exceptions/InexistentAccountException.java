package com.bbva.wallet.exceptions;

public class InexistentAccountException extends RuntimeException {
    private final String accountId;
    public InexistentAccountException(String message, String accountId) {
        super(message);
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
