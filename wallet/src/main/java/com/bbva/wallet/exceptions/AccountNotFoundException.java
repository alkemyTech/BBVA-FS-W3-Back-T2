package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.Currency;

public class AccountNotFoundException extends RuntimeException{
    Currency currency;

    public AccountNotFoundException(String message, Currency currency) {
        super(message);
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }
}
