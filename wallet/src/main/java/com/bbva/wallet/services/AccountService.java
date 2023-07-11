package com.bbva.wallet.services;

import com.bbva.wallet.dtos.UpdateAccount;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.InexistentAccountException;
import com.bbva.wallet.exceptions.NoUserAccountsException;
import com.bbva.wallet.exceptions.UserAccountMismatchException;
import com.bbva.wallet.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

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

    public Account updateAccount(User user, String accountId, UpdateAccount updateAccount) {
        List<Account> userAccounts = accountRepository.findByUser(user);
        Optional<Account> optionalAccount = accountRepository.findById(accountId);

        if (userAccounts.isEmpty()) {
            throw new NoUserAccountsException("No se encontraron cuentas para el usuario");
        }

        if (optionalAccount.isEmpty()) {
            throw new InexistentAccountException("No se encontró una cuenta con el ID especificado", accountId);
        }

        Account account = optionalAccount.get();

        if (!userAccounts.contains(account)) {
            throw new UserAccountMismatchException("La cuenta no pertenece al usuario especificado", accountId);
        }

        account.setTransactionLimit(updateAccount.getTransactionLimit());
        return accountRepository.save(account);
    }

}
