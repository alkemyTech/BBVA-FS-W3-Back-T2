package com.bbva.wallet.services;

import com.bbva.wallet.dtos.AccountBalance;
import com.bbva.wallet.dtos.AccountsBalance;
import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.dtos.TransactionHistory;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exceptions.AccountCreationException;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.utils.CbuUtil;
import org.springframework.stereotype.Service;
import com.bbva.wallet.exceptions.DeletedUserException;
import com.bbva.wallet.repositories.FixedTermDepositRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final FixedTermDepositRepository fixedTermDepositRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, TransactionRepository transactionRepository, FixedTermDepositRepository fixedTermDepositRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.fixedTermDepositRepository = fixedTermDepositRepository;
    }

    public Account createAccount(User user, Currency currency) {

        if (accountRepository.existsByUserAndCurrency(user, currency)) {
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

    private AccountBalance buildAccountBalance(Account account) {
        return AccountBalance.builder()
                .cbu(account.getCbu())
                .balance(account.getBalance())
                .build();
    }

    private List<TransactionDto> getTransactionsDto(Account account) {
        return transactionRepository.findByAccount(account)
                .stream()
                .map(TransactionDto::new)
                .toList();
    }

    private Optional<Account> filterAccountsByCurrency(List<Account> accounts, Currency currency) {
        return accounts.stream()
                .filter(account -> account.getCurrency().equals(currency))
                .findFirst();
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

    public AccountsBalance getAccountsBalance(User user) {
        if (user.isSoftDelete()) {
            throw new DeletedUserException("El usuario ha sido eliminado", user.getEmail());
        } else {
            List<Account> activeAccounts = accountRepository.findByUser(user);
            List<TransactionHistory> transactionHistory = new ArrayList<>();
            AccountsBalance accountsBalance = new AccountsBalance();
            Optional<Account> optionalAccountArs = filterAccountsByCurrency(activeAccounts, Currency.ARS);
            Optional<Account> optionalAccountUsd = filterAccountsByCurrency(activeAccounts, Currency.USD);

            if (optionalAccountArs.isPresent()) {
                Account accountArs = optionalAccountArs.get();
                AccountBalance accountBalanceArs = buildAccountBalance(accountArs);
                accountsBalance.setAccountArs(accountBalanceArs);

                List<TransactionDto> transactionArsDto = getTransactionsDto(accountArs);
                TransactionHistory transactionHistoryArs = new TransactionHistory(Currency.ARS, transactionArsDto);
                transactionHistory.add(transactionHistoryArs);

                List<FixedTermDeposit> fixedTermDepositsArs = fixedTermDepositRepository.findByAccount(accountArs);
                accountsBalance.setFixedTerms(fixedTermDepositsArs);
            }

            if (optionalAccountUsd.isPresent()) {
                Account accountUsd = optionalAccountUsd.get();
                AccountBalance accountBalanceUsd = buildAccountBalance(accountUsd);
                accountsBalance.setAccountUsd(accountBalanceUsd);

                List<TransactionDto> transactionUsdDto = getTransactionsDto(accountUsd);
                TransactionHistory transactionHistoryUsd = new TransactionHistory(Currency.USD, transactionUsdDto);
                transactionHistory.add(transactionHistoryUsd);
            }
            accountsBalance.setHistory(transactionHistory);
            return accountsBalance;
        }
    }
}

