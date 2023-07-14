package com.bbva.wallet.seeders;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.utils.CbuUtil;
import com.bbva.wallet.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountSeeder implements CommandLineRunner, Ordered {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;



    @Override
    public void run(String... args) throws Exception {
        List<User> users = userRepository.findFirst10();
        loadAccountData(users);
    }

    private void loadAccountData(List<User> users) {
        if (accountRepository.count() == 0) {
            int numUsersWithBothCurrencies = 5;

            for (User user : users) {
                String cbu = CbuUtil.generateCbu();
                while (accountRepository.findById(cbu).isPresent()) {
                    cbu = CbuUtil.generateCbu();
                }

                Currency currency = Currency.ARS;
                Double balance = RandomUtils.getRandom(0, 500_000);
                Double transactionLimit = RandomUtils.getRandom(400_000, 700_000);
                Account account = Account.builder()
                        .user(user)
                        .balance(balance)
                        .cbu(cbu)
                        .currency(currency)
                        .transactionLimit(transactionLimit)
                        .build();
                accountRepository.save(account);

                if (numUsersWithBothCurrencies > 0) {
                    cbu = CbuUtil.generateCbu();
                    while (accountRepository.findById(cbu).isPresent()) {
                        cbu = CbuUtil.generateCbu();
                    }

                    currency = Currency.USD;
                    balance = RandomUtils.getRandom(0, 10_000);
                    transactionLimit = RandomUtils.getRandom(1000, 2000);
                    account = Account.builder()
                            .user(user)
                            .balance(balance)
                            .cbu(cbu)
                            .currency(currency)
                            .transactionLimit(transactionLimit)
                            .build();
                    accountRepository.save(account);

                    numUsersWithBothCurrencies--;
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
