package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currency;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TransactionHistory {
    Currency currency;
    List<TransactionDto> transactions;
}
