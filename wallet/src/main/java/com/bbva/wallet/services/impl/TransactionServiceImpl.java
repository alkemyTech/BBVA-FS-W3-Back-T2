package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.TransactionInputDto;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.TransactionType;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.services.TransactionService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {


    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;



    @SneakyThrows
    @Transactional
    public boolean SendArs(Long SenderUserID, TransactionInputDto Receiver) {


        var SenderUser = userRepository.findById(SenderUserID).get();
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
    public boolean SendUsd(Long SenderUserID, TransactionInputDto Receiver) {


        var SenderUser = userRepository.findById(SenderUserID).get();
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

}
