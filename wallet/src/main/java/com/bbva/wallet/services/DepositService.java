package com.bbva.wallet.services;

import com.bbva.wallet.dtos.DepositRequest;
import com.bbva.wallet.dtos.DepositResponse;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.TransactionType;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public DepositResponse deposit(DepositRequest depositRequest, HttpServletRequest request){
        //conseguir mail
        String authorizationHeader = request.getHeader("Authorization");
        String emailUser = jwtService.extractUserName(authorizationHeader.substring(7));

        //encontrar la cuenta
        Account cuenta = findAccount(depositRequest.getCurrency(), emailUser);

        //sumar el balance
        Double balance = cuenta.getBalance() + depositRequest.getAmount();
        cuenta.setBalance(balance);
        accountRepository.save(cuenta);

        //crear la transaccion
        Transaction depositTransaction = Transaction.builder()
                .name(TransactionType.DEPOSIT)
                .amount(depositRequest.getAmount())
                .account(cuenta)
                .build();
        transactionRepository.save(depositTransaction);

        //retornar el depositResponse
        return DepositResponse.builder().transaction(depositTransaction).updatedAccount(cuenta).build();

    }

    public Account findAccount(Currency currency, String emailUser){

        User user = userRepository.findByEmail(emailUser).get();
        List<Account> userAccounts= accountService.findAccountsByUser(user.getId());
        List<Account> accounts = userAccounts.stream().filter(c-> c.getCurrency() == currency).collect(Collectors.toList());

        if (accounts.size() == 1) {
            return accounts.get(0);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no tiene una cuenta en " + currency);
        }

    }

    }


