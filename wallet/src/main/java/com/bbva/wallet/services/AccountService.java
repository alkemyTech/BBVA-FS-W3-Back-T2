package com.bbva.wallet.services;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exceptions.AccountCreationException;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.utils.CbuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bbva.wallet.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    public Account createAccount(User user, Currency currency) {

        if (accountRepository.findByUserAndCurrency(user, currency)) {
            throw new AccountCreationException("Ya existe una cuenta del mismo tipo para este usuario.");
        }

        String cbu = CbuUtil.generateCbu();

        double transaction = currency == Currency.ARS ? 300000.0 : 1000.0;

        Account account = buildAccount(cbu, currency, transaction, user);

        return accountRepository.save(account);
    }
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

    public List<Account> findAccountsByUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return accountRepository.findByUser(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }
}

