package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currency;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PaymentRequest {
    @Positive(message = "El monto a pagar no puede ser inferior a cero.")
    private Double amount;

    private Currency currency;

    @Nullable
    @Size(max=100)
    private String description;
}
