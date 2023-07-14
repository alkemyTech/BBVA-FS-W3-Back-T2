package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.Currency;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String message) {
        super(message);
    }
}
