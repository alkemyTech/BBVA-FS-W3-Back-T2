package com.bbva.wallet.exceptions;

public class UserAccountMismatchException extends RuntimeException {
    private final String accountId;

    public UserAccountMismatchException(String message, String accountId) {
        super(message);
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
