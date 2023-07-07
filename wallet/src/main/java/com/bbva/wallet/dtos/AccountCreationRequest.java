package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountCreationRequest {
    @NotBlank(message = "Debe indicar el tipo de moneda para la cuenta")
    private Currency currency;
}
