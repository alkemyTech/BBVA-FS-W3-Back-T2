package com.bbva.wallet.exceptions;

public class DuplicateEmailException extends RuntimeException {
    private final String duplicateEmail;

    public DuplicateEmailException(String message, String duplicateEmail) {
        super(message);
        this.duplicateEmail = duplicateEmail;
    }

    public String getDuplicateEmail() {
        return duplicateEmail;
    }
}

