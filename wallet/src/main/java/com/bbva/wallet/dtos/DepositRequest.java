package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currency;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequest {

    @NotNull(message = "Debe indicar el tipo de moneda para el depósito.")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    @Positive(message = "El valor del depósito debe ser superior a 0.")
    private Double amount;

    @Nullable
    @Size(max=100)
    private String description;
}