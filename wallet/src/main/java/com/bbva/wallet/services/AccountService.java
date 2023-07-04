package com.bbva.wallet.services;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account buildAccount(String cbu, Currency currency, double transactionLimit, User user) {
        return Account.builder()
                .user(user)
                .balance(0.0)
                .cbu(cbu)
                .currency(currency)
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .transactionLimit(transactionLimit)
                .build();
    }
}
