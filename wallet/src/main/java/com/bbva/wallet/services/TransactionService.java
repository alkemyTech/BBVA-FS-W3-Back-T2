package com.bbva.wallet.services;

import com.bbva.wallet.dtos.Payment;
import com.bbva.wallet.dtos.PaymentRegister;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.TransactionType;
import com.bbva.wallet.exceptions.AccountNotFoundException;
import com.bbva.wallet.exceptions.InsufficientFundsException;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public Transaction buildTransaction(Account account, Double amount, TransactionType transactionType, String description) {
        return Transaction.builder()
                .account(account)
                .amount(amount)
                .name(transactionType)
                .description(description)
                .build();
    }

    public PaymentRegister pay(User user, Payment payment) {
        Double amount = payment.getAmount();
        Currency currency = payment.getCurrency();
        PaymentRegister paymentRegister = new PaymentRegister();
        Optional<Account> optionalAccount = accountRepository.findByUserAndCurrency(user, currency);
        optionalAccount.ifPresent(account -> {
            if (account.getBalance() >= amount) {
                Transaction transaction = buildTransaction(account, amount, TransactionType.PAYMENT, "Payment");
                account.setBalance(account.getBalance() - amount);
                paymentRegister.setTransaction(transactionRepository.save(transaction));
                paymentRegister.setAccount(account);
            } else {
                throw new InsufficientFundsException("Fondos insuficientes");
            }
        });
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException("No se ha encontrado una cuenta en la moneda indicada", currency);
        }
        return paymentRegister;
    }

}
