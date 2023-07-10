package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.Transaction;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AccountsBalance {
    private AccountBalance accountArs;
    private AccountBalance accountUsd;
    private List<TransactionHistory> history;
    private List<FixedTermDeposit> fixedTerms;
}
