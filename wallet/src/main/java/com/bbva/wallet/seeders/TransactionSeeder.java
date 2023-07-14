package com.bbva.wallet.seeders;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.TransactionType;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionSeeder implements CommandLineRunner, Ordered {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (transactionRepository.count() == 0) {
            List<Account> accounts = accountRepository.findAll();
            generateTransactions(accounts);
        }
    }

    private void generateTransactions(List<Account> accounts) {
        TransactionType[] transactionTypes = TransactionType.values();

        for (int i = 0; i < 25; i++) {
            Account sender = getRandomAccount(accounts);
            Account receiver = getRandomAccount(accounts);

            while (sender.equals(receiver)) {
                receiver = getRandomAccount(accounts);
            }

            Currency currency = sender.getCurrency();
            double amount = RandomUtils.getRandom(1, 10000);

            TransactionType transactionType = transactionTypes[(int) RandomUtils.getRandom(0, transactionTypes.length - 1)];

            Transaction transaction = Transaction.builder()
                    .amount(amount)
                    .name(transactionType)
                    .description("Transaction " + (i + 1))
                    .account(receiver)
                    .build();

            transactionRepository.save(transaction);
        }
    }

    private Account getRandomAccount(List<Account> accounts) {
        int index = (int) RandomUtils.getRandom(0, accounts.size() - 1);
        return accounts.get(index);
    }

    @Override
    public int getOrder() {
        return 4;
    }
}

