package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currency;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TransactionHistoryResponse {
    private Currency currency;
    private List<TransactionResponseDTO> transactions;
}
