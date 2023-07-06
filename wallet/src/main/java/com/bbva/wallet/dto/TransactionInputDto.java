package com.bbva.wallet.dto;

import com.bbva.wallet.enums.TransactionType;
import lombok.Data;

@Data
public class TransactionInputDto {

    private String cbu;
    private Double amount;

}
