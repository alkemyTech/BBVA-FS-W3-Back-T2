package com.bbva.wallet.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsuficientBalanceException extends RuntimeException{

    private String cbu;
    public InsuficientBalanceException(String message, String cbu) {
        super(message);
        this.cbu = cbu;
    }

}