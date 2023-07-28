package com.bbva.wallet.dtos;

import lombok.Data;

@Data
public class TransactionRequestDTO {

    private String cbu;
    private Double amount;

}
