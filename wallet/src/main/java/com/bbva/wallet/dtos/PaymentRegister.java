package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRegister {
    private Transaction transaction;
    private Account account;
}
