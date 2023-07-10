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
    AccountBalance accountArs;
    AccountBalance accountUsd;
    List<TransactionHistory> history;
    List<FixedTermDeposit> fixedTerms;
}
