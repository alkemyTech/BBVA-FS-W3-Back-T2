package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountCreationRequest {
    private Currency currency;
}