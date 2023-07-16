package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currency;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Payment {
    @Positive(message = "El monto a pagar no puede ser inferior a cero.")
    private Double amount;
    private Currency currency;
}
