package com.bbva.wallet.services;

import com.bbva.wallet.dtos.*;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Role;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.enums.TransactionType;
import com.bbva.wallet.exceptions.*;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.RoleRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
public class TransactionService {

    private UserRepository userRepository;

    private TransactionRepository transactionRepository;

    private AccountRepository accountRepository;

    private RoleRepository roleRepository;

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @SneakyThrows
    @Transactional
    public Transaction sendArs(String username, TransactionRequestDTO Receiver) {
        Optional<Account> cbu = accountRepository.findById(Receiver.getCbu());
        if (cbu.isEmpty())
            throw new NotExistentCbuException("El CBU no existe.", Receiver.getCbu());

        var senderUser = userRepository.findByEmail(username).get();
        var senderAccount = accountRepository.findByUserAndCurrency(senderUser, Currency.ARS).get();
        var receiverAccount = accountRepository.findById(Receiver.getCbu()).get();


        if (receiverAccount.getCurrency() != Currency.ARS) {
            throw new TransactionNotFoundAccountException("No es una cuenta en pesos");
        }

        if (senderAccount.getUser() == receiverAccount.getUser()) {
            throw new TransactionFailedForSameUserException("No se puede realizar transferencias al mismo usuario");
        }

        if (senderAccount.getBalance() < Receiver.getAmount()) {
            throw new InsufficientFundsException("Saldo insuficiente");
        }

        var payerTransaction = new Transaction();

        payerTransaction.setAmount(Receiver.getAmount());
        payerTransaction.setCreationDate(LocalDateTime.now());
        payerTransaction.setUpdatedDate(LocalDateTime.now());
        payerTransaction.setName(TransactionType.PAYMENT);
        payerTransaction.setAccount(senderAccount);
        payerTransaction.setDescription("Transferencia al cbu: " + receiverAccount.getCbu());


        var incomeTransaction = new Transaction();

        incomeTransaction.setAmount(Receiver.getAmount());
        incomeTransaction.setCreationDate(LocalDateTime.now());
        incomeTransaction.setUpdatedDate(LocalDateTime.now());
        incomeTransaction.setName(TransactionType.INCOME);
        incomeTransaction.setAccount(receiverAccount);
        incomeTransaction.setDescription("Transferencia del cbu: " + senderAccount.getCbu());

        transactionRepository.save(payerTransaction);
        transactionRepository.save(incomeTransaction);


        senderAccount.setBalance(senderAccount.getBalance() - Receiver.getAmount());
        receiverAccount.setBalance(receiverAccount.getBalance() + Receiver.getAmount());

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        return payerTransaction;
    }

    @SneakyThrows
    @Transactional
    public Transaction sendUsd(String username, TransactionRequestDTO Receiver) {


        var senderUser = userRepository.findByEmail(username).get();
        var senderAccount = accountRepository.findByUserAndCurrency(senderUser, Currency.USD).get();
        var receiverAccount = accountRepository.findById(Receiver.getCbu()).get();
        Optional<Account> cbu = accountRepository.findById(Receiver.getCbu());

        if (cbu.isEmpty()) {
            throw new NotExistentCbuException("El CBU no existe.", Receiver.getCbu());
        }

        if (receiverAccount.getCurrency() != Currency.USD) {
            throw new TransactionNotFoundAccountException("No es una cuenta en dólares");
        }

        if (senderAccount.getUser() == receiverAccount.getUser()) {
            throw new TransactionFailedForSameUserException("No se puede realizar transferencias al mismo usuario");
        }

        if (senderAccount.getBalance() < Receiver.getAmount()) {
            throw new InsufficientFundsException("Saldo insuficiente");
        }

        var payerTransaction = new Transaction();

        payerTransaction.setAmount(Receiver.getAmount());
        payerTransaction.setCreationDate(LocalDateTime.now());
        payerTransaction.setUpdatedDate(LocalDateTime.now());
        payerTransaction.setName(TransactionType.PAYMENT);
        payerTransaction.setAccount(senderAccount);
        payerTransaction.setDescription("Transferencia al cbu: " + receiverAccount.getCbu());

        var incomeTransaction = new Transaction();

        incomeTransaction.setAmount(Receiver.getAmount());
        incomeTransaction.setCreationDate(LocalDateTime.now());
        incomeTransaction.setUpdatedDate(LocalDateTime.now());
        incomeTransaction.setName(TransactionType.INCOME);
        incomeTransaction.setAccount(receiverAccount);
        incomeTransaction.setDescription("Transferencia del cbu: " + senderAccount.getCbu());

        transactionRepository.save(payerTransaction);
        transactionRepository.save(incomeTransaction);

        senderAccount.setBalance(senderAccount.getBalance() - Receiver.getAmount());
        receiverAccount.setBalance(receiverAccount.getBalance() + Receiver.getAmount());

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        return payerTransaction;
    }

    public Transaction updateTransaction(User user, Long id, UpdateTransactionRequest updateTransactionRequest) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);

        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();

            List<Account> accounts = accountRepository.findByUser(user);
            if (!accounts.isEmpty() && accounts.contains(transaction.getAccount())) {
                transaction.setDescription(updateTransactionRequest.getDescription());
                return transactionRepository.save(transaction);
            } else {
                throw new UserTransactionMismatchException("La transacción no pertenece al usuario", id);
            }
        } else {
            throw new NonexistentTransactionException("No existen transacciones con el id especificado", id);
        }
    }

    @GetMapping("/{userId}")
    public List<Transaction> getTransactionsById(Long userId, String email) {
        Optional<User> byEmail = this.userRepository.findByEmail(email);

        if (byEmail.isPresent()) {
            Optional<Role> byId = this.roleRepository.findById(byEmail.get().getRoleId().getId());
            if (Objects.equals(byEmail.get().getId(), userId)) {
                return this.transactionRepository.getTransactionsById(userId);
            } else if (byId.get().getName() == RoleName.ADMIN) {
                return this.transactionRepository.getTransactionsById(userId);
            } else {
                throw new ProhibitedAccessToTransactionsException("No esta permitido acceder");
            }
        }

        return null;
    }

    public Transaction buildTransaction(Account account, Double amount, TransactionType transactionType, String description) {
        return Transaction.builder()
                .account(account)
                .amount(amount)
                .name(transactionType)
                .description(description)
                .build();
    }

    public PaymentResponse pay(User user, PaymentRequest paymentRequest) {

        Double amount = paymentRequest.getAmount();
        Currency currency = paymentRequest.getCurrency();
        String description = paymentRequest.getDescription();

        PaymentResponse paymentResponse = new PaymentResponse();
        Optional<Account> optionalAccount = accountRepository.findByUserAndCurrency(user, currency);
        optionalAccount.ifPresent(account -> {
            if (account.getBalance() >= amount) {
                Transaction transaction = buildTransaction(account, amount, TransactionType.PAYMENT, description);
                account.setBalance(account.getBalance() - amount);
                paymentResponse.setTransaction(transactionRepository.save(transaction));
                paymentResponse.setAccount(account);

            } else {
                throw new InsufficientFundsException("Fondos insuficientes");
            }
        });
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException("No se ha encontrado una cuenta en la moneda indicada", currency);
        }
        return paymentResponse;
    }


    public PageTransactionResponse findAllTransaction(int page) {
        int pageSize = 10;
        PageTransactionResponse pageTransactionResponse = new PageTransactionResponse();
        Pageable pageable = PageRequest.of(page, pageSize);

        //contenido de página
        Page<Transaction> transactionsPage = transactionRepository.findAll(pageable);
        pageTransactionResponse.setTransactions(transactionsPage.getContent().stream().map(TransactionResponseDTO::new).toList());

        //valida que la página tenga contenido
        if (pageTransactionResponse.getTransactions().size() == 0)
            throw new InvalidUrlRequestException("La página buscada no se encuentra disponible.");

        //url de página siguiente
        if (transactionsPage.hasNext()) {
            int nextPage = transactionsPage.getNumber() + 1;
            String nextPageUrl = "/transactions?page=" + nextPage;
            pageTransactionResponse.setNextPageUrl(nextPageUrl);
        }

        //url de página anterior
        if (transactionsPage.hasPrevious()) {
            int previousPage = transactionsPage.getNumber() - 1;
            String previousPageUrl = "/transactions?page=" + previousPage;
            pageTransactionResponse.setPreviousPageUrl(previousPageUrl);
        }

        return pageTransactionResponse;
    }


}