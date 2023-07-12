package com.bbva.wallet.services;

import com.bbva.wallet.dtos.TransactionInputDto;
import com.bbva.wallet.dtos.UpdateTransaction;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.TransactionType;
import com.bbva.wallet.exceptions.NonexistentTransactionException;
import com.bbva.wallet.exceptions.UserTransactionMismatchException;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {


    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;



    @SneakyThrows
    @Transactional
    public boolean sendArs(String username, TransactionInputDto Receiver) {


        var SenderUser = userRepository.findByEmail(username).get();
        var SenderAccount = accountRepository.findByUserAndCurrency(SenderUser, Currency.ARS);
        var ReceiverAccount = accountRepository.findById(Receiver.getCbu()).get();

        if (ReceiverAccount.getCurrency() != Currency.ARS){
            throw new Exception("no es una cuenta en Pesos");
        }


        if (SenderAccount.getUser() == ReceiverAccount.getUser()){
            throw new Exception("no se puede enviar transactiones al mismo usuario");
        }

        if (SenderAccount.getBalance()<Receiver.getAmount()){
            throw new Exception("saldo insuficiente");
        }

        var Description = "transaction del cbu: "+SenderAccount.getCbu()+ " al cbu: "+ReceiverAccount.getCbu();

        var payerTransaction = new Transaction();

        payerTransaction.setAmount(Receiver.getAmount());
        payerTransaction.setCreationDate(LocalDateTime.now());
        payerTransaction.setUpdatedDate(LocalDateTime.now());
        payerTransaction.setName(TransactionType.PAYMENT);
        payerTransaction.setAccount(SenderAccount);
        payerTransaction.setDescription(Description);


        var incomeTransaction = new Transaction();

        incomeTransaction.setAmount(Receiver.getAmount());
        incomeTransaction.setCreationDate(LocalDateTime.now());
        incomeTransaction.setUpdatedDate(LocalDateTime.now());
        incomeTransaction.setName(TransactionType.INCOME);
        incomeTransaction.setAccount(ReceiverAccount);
        incomeTransaction.setDescription(Description);

        transactionRepository.save(payerTransaction);
        transactionRepository.save(incomeTransaction);

        SenderAccount.setBalance(SenderAccount.getBalance()-Receiver.getAmount());
        ReceiverAccount.setBalance(ReceiverAccount.getBalance()+Receiver.getAmount());

        accountRepository.save(SenderAccount);
        accountRepository.save(ReceiverAccount);

        return true;
    }

    @SneakyThrows
    @Transactional
    public boolean sendUsd(String username, TransactionInputDto Receiver) {


        var SenderUser = userRepository.findByEmail(username).get();
        var SenderAccount = accountRepository.findByUserAndCurrency(SenderUser, Currency.USD);
        var ReceiverAccount = accountRepository.findById(Receiver.getCbu()).get();

        if (ReceiverAccount.getCurrency() != Currency.USD){
            throw new Exception("no es una cuenta en Pesos");
        }


        if (SenderAccount.getUser() == ReceiverAccount.getUser()){
            throw new Exception("no se puede enviar transactiones al mismo usuario");
        }

        if (SenderAccount.getBalance()<Receiver.getAmount()){
            throw new Exception("saldo insuficiente");
        }

        var Description = "transaction del cbu: "+SenderAccount.getCbu()+ " al cbu: "+ReceiverAccount.getCbu();

        var payerTransaction = new Transaction();

        payerTransaction.setAmount(Receiver.getAmount());
        payerTransaction.setCreationDate(LocalDateTime.now());
        payerTransaction.setUpdatedDate(LocalDateTime.now());
        payerTransaction.setName(TransactionType.PAYMENT);
        payerTransaction.setAccount(SenderAccount);
        payerTransaction.setDescription(Description);


        var incomeTransaction = new Transaction();

        incomeTransaction.setAmount(Receiver.getAmount());
        incomeTransaction.setCreationDate(LocalDateTime.now());
        incomeTransaction.setUpdatedDate(LocalDateTime.now());
        incomeTransaction.setName(TransactionType.INCOME);
        incomeTransaction.setAccount(ReceiverAccount);
        incomeTransaction.setDescription(Description);

        transactionRepository.save(payerTransaction);
        transactionRepository.save(incomeTransaction);

        SenderAccount.setBalance(SenderAccount.getBalance()-Receiver.getAmount());
        ReceiverAccount.setBalance(ReceiverAccount.getBalance()+Receiver.getAmount());

        accountRepository.save(SenderAccount);
        accountRepository.save(ReceiverAccount);

        return true;
    }

    public Transaction updateTransaction(User user, Long id, UpdateTransaction updateTransaction) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);

        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();

            List<Account> accounts = accountRepository.findByUser(user);
            if (!accounts.isEmpty() && accounts.contains(transaction.getAccount())) {
                transaction.setDescription(updateTransaction.getDescription());
                return transactionRepository.save(transaction);
            } else {
                throw new UserTransactionMismatchException("La transacción no pertenece al usuario", id);
            }
        } else {
            throw new NonexistentTransactionException("No existen transacciones con el id especificado", id);
        }
    }

}
