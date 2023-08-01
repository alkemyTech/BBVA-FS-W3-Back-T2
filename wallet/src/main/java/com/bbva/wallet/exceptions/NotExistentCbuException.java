package com.bbva.wallet.exceptions;


public class NotExistentCbuException extends RuntimeException{
    private final String cbu;
    public NotExistentCbuException(String message, String cbu) {
        super(message);
        this.cbu = cbu;

    }

    public String getCbu() { return cbu;}
}
