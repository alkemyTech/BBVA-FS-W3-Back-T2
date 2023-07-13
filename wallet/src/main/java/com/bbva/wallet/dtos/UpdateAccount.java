package com.bbva.wallet.dtos;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateAccount {
    @Positive(message = "No se admiten valores negativos")
    private Double transactionLimit;
}
