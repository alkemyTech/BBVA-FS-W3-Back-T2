package com.bbva.wallet.services;

import com.bbva.wallet.dtos.Payment;
import com.bbva.wallet.dtos.PaymentRegister;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.TransactionType;
import com.bbva.wallet.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository accountRepository;

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
        Optional<Account> accountOptional = accountRepository.findByUserAndCurrency(user, currency);
        if (accountOptional.isPresent() && accountOptional.get().getBalance() >= amount) {
            Account account = accountOptional.get();
            Transaction transaction = buildTransaction(account, amount, TransactionType.PAYMENT, "Payment");
            account.setBalance(account.getBalance() - amount);
            paymentRegister.setTransaction(transaction);
            paymentRegister.setAccount(account);
        } else {
            System.out.println("Todo mal.");
        }
        return paymentRegister;
    }

}
