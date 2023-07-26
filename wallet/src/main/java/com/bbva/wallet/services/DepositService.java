package com.bbva.wallet.services;

import com.bbva.wallet.dtos.DepositRequest;
import com.bbva.wallet.dtos.DepositResponse;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.TransactionType;
import com.bbva.wallet.exceptions.AccountNotFoundException;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositService{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JwtService jwtService;
    public DepositResponse deposit(DepositRequest depositRequest, Authentication authentication){
        //conseguir user y cuenta
        User user = (User) authentication.getPrincipal();
        Account cuenta = findAccount(depositRequest.getCurrency(), user.getEmail());

        //sumar el balance
        Double balance = cuenta.getBalance() + depositRequest.getAmount();
        cuenta.setBalance(balance);
        accountRepository.save(cuenta);

        //crear la transaccion
        Transaction depositTransaction = Transaction.builder()
                .name(TransactionType.DEPOSIT)
                .amount(depositRequest.getAmount())
                .account(cuenta)
                .description(depositRequest.getDescription())
                .build();
        transactionRepository.save(depositTransaction);

        //retornar el depositResponse
        return DepositResponse.builder().transaction(depositTransaction).updatedAccount(cuenta).build();

    }

    public Account findAccount(Currency currency, String emailUser){

        User user = userRepository.findByEmail(emailUser).get();
        List<Account> userAccounts= accountService.findAccountsByUser(user.getId());

        return userAccounts.stream()
                .filter(c -> c.getCurrency() == currency && !c.isSoftDelete())
                .findAny()
                .orElseThrow(() -> new AccountNotFoundException("El usuario no tiene una cuenta en " + currency, currency));

    }

    }


