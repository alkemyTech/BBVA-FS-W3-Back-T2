package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AccountNotFoundException extends RuntimeException{
    Currency currency;

    public AccountNotFoundException(String message, Currency currency) {
        super(message);
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class AccountNotFoundException extends RuntimeException{
//
//    private Long userId;
//    public AccountNotFoundException(String message, Long id) {
//        super(message);
//        this.userId = id;
//    }
//
}
