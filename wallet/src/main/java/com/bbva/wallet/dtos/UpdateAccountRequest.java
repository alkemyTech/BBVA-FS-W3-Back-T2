package com.bbva.wallet.dtos;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateAccountRequest {
    @Positive(message = "No se admiten valores negativos")
    private Double transactionLimit;
}
