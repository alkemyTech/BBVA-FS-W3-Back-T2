package com.bbva.wallet.services;

import com.bbva.wallet.dtos.AccountCreationRequest;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exceptions.AccountCreationException;
import com.bbva.wallet.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(User user, AccountCreationRequest accountCreationRequest) {

        if (accountRepository.existsByUserAndCurrency(user, accountCreationRequest.getCurrency().name())) {
            throw new AccountCreationException("Ya existe una cuenta para este usuario del mismo tipo.");
        }

        Account account = new Account();
        account.setUser(user);
        account.setCurrency(accountCreationRequest.getCurrency());
        account.setBalance(0.0);

        if (accountCreationRequest.getCurrency() == Currency.ARS) {
            account.setTransactionLimit(300000.0);
        } else if (accountCreationRequest.getCurrency() == Currency.USD) {
            account.setTransactionLimit(1000.0);
        }

        return accountRepository.save(account);
    }
}
