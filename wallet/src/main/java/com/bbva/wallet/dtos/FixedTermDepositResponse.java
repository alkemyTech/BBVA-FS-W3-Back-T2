package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixedTermDepositResponse {

    private Account updatedAccount;
    private FixedTermDeposit fixedTermDeposit;

}
