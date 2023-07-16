package com.bbva.wallet.exceptions;

public class DeletedUserException extends RuntimeException {
    private final String deletedUser;

    public DeletedUserException(String message, String deletedUser) {
        super(message);
        this.deletedUser = deletedUser;
    }

    public String getDeletedUser() {
        return deletedUser;
    }
}
