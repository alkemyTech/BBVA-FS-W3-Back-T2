package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

}