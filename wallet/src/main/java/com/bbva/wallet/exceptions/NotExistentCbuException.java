package com.bbva.wallet.exceptions;

import com.bbva.wallet.entities.User;

public class NotExistentCbuException extends RuntimeException{
    private final String cbu;
    private User firstName;

    public NotExistentCbuException(String message, String cbu) {
        super(message);
        this.cbu = cbu;
        this.firstName = firstName;
    }

    public String getCbu() { return cbu;}
}
