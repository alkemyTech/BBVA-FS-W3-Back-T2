package com.bbva.wallet.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountNotFoundException extends RuntimeException{

    private Long userId;
    public AccountNotFoundException(String message, Long id) {
        super(message);
        this.userId = id;
    }

}
