package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposit;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AccountsBalanceResponse {
    private AccountBalanceDto accountArs;
    private AccountBalanceDto accountUsd;
    private List<TransactionHistoryResponse> history;
    private List<FixedTermDeposit> fixedTerms;


}
