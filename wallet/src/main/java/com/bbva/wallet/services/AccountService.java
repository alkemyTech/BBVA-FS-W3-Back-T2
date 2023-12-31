package com.bbva.wallet.services;

import com.bbva.wallet.dtos.*;
import com.bbva.wallet.dtos.UpdateAccountRequest;
import com.bbva.wallet.dtos.AccountBalanceDto;
import com.bbva.wallet.dtos.AccountsBalanceResponse;
import com.bbva.wallet.dtos.TransactionResponseDTO;
import com.bbva.wallet.dtos.TransactionHistoryResponse;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.*;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.utils.CbuUtil;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.bbva.wallet.repositories.FixedTermDepositRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Pageable;
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

    private AccountBalanceDto buildAccountBalance(Account account) {
        return AccountBalanceDto.builder()
                .cbu(account.getCbu())
                .balance(account.getBalance())
                .build();
    }

    private List<TransactionResponseDTO> getTransactionsDto(Account account) {
        return transactionRepository.findByAccount(account)
                .stream()
                .map(TransactionResponseDTO::new)
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

    public Account updateAccount(User user, String accountId, UpdateAccountRequest updateAccountRequest) {
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

        account.setTransactionLimit(updateAccountRequest.getTransactionLimit());
        return accountRepository.save(account);
    }

    public AccountsBalanceResponse getAccountsBalance(User user) {
        if (user.isSoftDelete()) {
            throw new DeletedUserException("El usuario ha sido eliminado", user.getEmail());
        } else {
            List<Account> activeAccounts = accountRepository.findByUser(user);
            List<TransactionHistoryResponse> transactionHistoryResponse = new ArrayList<>();
            AccountsBalanceResponse accountsBalanceResponse = new AccountsBalanceResponse();
            Optional<Account> optionalAccountArs = filterAccountsByCurrency(activeAccounts, Currency.ARS);
            Optional<Account> optionalAccountUsd = filterAccountsByCurrency(activeAccounts, Currency.USD);

            if (optionalAccountArs.isPresent()) {
                Account accountArs = optionalAccountArs.get();
                AccountBalanceDto accountBalanceDtoArs = buildAccountBalance(accountArs);
                accountsBalanceResponse.setAccountArs(accountBalanceDtoArs);

                List<TransactionResponseDTO> transactionArsDto = getTransactionsDto(accountArs);
                TransactionHistoryResponse transactionHistoryResponseArs = new TransactionHistoryResponse(Currency.ARS, transactionArsDto);
                transactionHistoryResponse.add(transactionHistoryResponseArs);

                List<FixedTermDeposit> fixedTermDepositsArs = fixedTermDepositRepository.findByAccount(accountArs);
                accountsBalanceResponse.setFixedTerms(fixedTermDepositsArs);
            }

            if (optionalAccountUsd.isPresent()) {
                Account accountUsd = optionalAccountUsd.get();
                AccountBalanceDto accountBalanceDtoUsd = buildAccountBalance(accountUsd);
                accountsBalanceResponse.setAccountUsd(accountBalanceDtoUsd);

                List<TransactionResponseDTO> transactionUsdDto = getTransactionsDto(accountUsd);
                TransactionHistoryResponse transactionHistoryResponseUsd = new TransactionHistoryResponse(Currency.USD, transactionUsdDto);
                transactionHistoryResponse.add(transactionHistoryResponseUsd);
            }
            accountsBalanceResponse.setHistory(transactionHistoryResponse);
            return accountsBalanceResponse;
        }
    }

    public PageAccountResponse findAllAccounts(int page) {
        int pageSize = 10;
        PageAccountResponse pageAccountResponse = new PageAccountResponse();
        Pageable pageable = PageRequest.of(page, pageSize);

        //contenido de página
        Page<Account> accountsPage = accountRepository.findAll(pageable);
        pageAccountResponse.setAccounts(accountsPage.getContent().stream().map(AccountDto:: new).toList());

        //valida que la página tenga contenido
        if(pageAccountResponse.getAccounts().size() == 0)
            throw new InvalidUrlRequestException("La página buscada no se encuentra disponible.");

        //url de página siguiente
        if (accountsPage.hasNext()) {
            int nextPage = accountsPage.getNumber() + 1;
            String nextPageUrl = "/accounts?page=" + nextPage;
            pageAccountResponse.setNextPageUrl(nextPageUrl);
        }

        //url de página anterior
        if (accountsPage.hasPrevious()) {
            int previousPage = accountsPage.getNumber() - 1;
            String previousPageUrl = "/accounts?page=" + previousPage;
            pageAccountResponse.setPreviousPageUrl(previousPageUrl);
        }

        return pageAccountResponse;
    }

    @SneakyThrows
    public CbuAuthenticateResponse cbuAuthenticateByCurrency(CbuAuthenticateRequest cbuAuthenticateRequest) {
        Account byCbu = this.accountRepository.findByCbu(cbuAuthenticateRequest.getCbu());
        if (byCbu != null) {
            if (byCbu.getCurrency().equals(cbuAuthenticateRequest.getCurrency()) ) {

                CbuAuthenticateResponse response = new CbuAuthenticateResponse();
                response.setCbu(byCbu.getCbu());
                response.setUser(byCbu.getUser());
                return response;
            }
            throw new AccountNotFoundException("El tipo de cuenta no coincide");
        }
        throw new AccountNotFoundException("No existe una cuenta asociada a ese cbu");
    }
}

