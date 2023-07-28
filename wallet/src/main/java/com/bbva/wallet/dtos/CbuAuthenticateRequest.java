package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currency;
import lombok.Data;

@Data
public class CbuAuthenticateRequest {
    private Currency currency;
    private String cbu;
}
