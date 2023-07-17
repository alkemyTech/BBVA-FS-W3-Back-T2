package com.bbva.wallet.services;

import com.bbva.wallet.dtos.FixedTermDepositRequest;
import com.bbva.wallet.dtos.FixedTermDepositResponse;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposit;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exceptions.InsuficientBalanceException;
import com.bbva.wallet.exceptions.AccountNotFoundException;
import com.bbva.wallet.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.bbva.wallet.repositories.FixedTermDepositRepository;
import com.bbva.wallet.repositories.UserRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class FixedTermDepositService {
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final FixedTermDepositRepository fixedTermDepositRepository;
    private final UserRepository userRepository;

    @Value("${porcentaje.interes.fixed.term.deposit}")
    double interestPorcentage;

    public FixedTermDepositService(AccountRepository accountRepository, JwtService jwtService, FixedTermDepositRepository fixedTermDepositRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
        this.fixedTermDepositRepository = fixedTermDepositRepository;
        this.userRepository = userRepository;
    }

    public FixedTermDepositResponse createFixedTermDeposit(FixedTermDepositRequest fixedTermDepositRequest, Authentication authentication){

        //conseguir mail y cuenta en pesos
        User user = (User) authentication.getPrincipal();
        Account account = findArsAccount(user);

        // Verificar que la cuenta tiene balance suficiente
        if (account.getBalance() < fixedTermDepositRequest.getAmount()) {
            throw new InsuficientBalanceException("Balance insuficiente en la cuenta.", account.getCbu());
        }

        // Restar el monto del plazo fijo del balance de la cuenta y setearlo
        Double balanceFinal = account.getBalance() - fixedTermDepositRequest.getAmount();
        account.setBalance(balanceFinal);
        accountRepository.save(account);

        // Calcular fechas y montos
        Date creationDate = new Date();
        Date closingDate = calculateClosingDate(creationDate, fixedTermDepositRequest.getTotalDays());
        Long totalMsBetweenDates = closingDate.getTime() - creationDate.getTime();
        Double interest = calcularInteres(fixedTermDepositRequest.getAmount(),
                totalMsBetweenDates);

        // Crear y guardar el plazo fijo en base de datos
        FixedTermDeposit fixedTermDeposit = FixedTermDeposit.builder()
                .amount(fixedTermDepositRequest.getAmount())
                .account((account))
                .interest(interest)
                .closingDate(closingDate)
                .build();
        fixedTermDepositRepository.save(fixedTermDeposit);

        //Crea y retorna el response plazo fijo
        return FixedTermDepositResponse.builder().
                fixedTermDeposit(fixedTermDeposit).
                updatedAccount(account).
                build();

    }

    public FixedTermDepositResponse simulateFixedTermDeposit(FixedTermDepositRequest fixedTermRequest, Authentication authentication){

        //conseguir mail y cuenta en pesos
        User user = (User) authentication.getPrincipal();
        Account account = findArsAccount(user);

        // Calcular balance final cuenta
        Double balanceFinal = account.getBalance() - fixedTermRequest.getAmount();
        account.setBalance(balanceFinal);

        // Verificar que la cuenta tiene balance suficiente
        if (account.getBalance() < fixedTermRequest.getAmount()) {
            throw new InsuficientBalanceException("Balance insuficiente en la cuenta.", account.getCbu());
        }

        // Calcular fechas y montos
        Date creationDate = new Date();
        Date closingDate = calculateClosingDate(creationDate, fixedTermRequest.getTotalDays());
        Long totalMsBetweenDates = closingDate.getTime() - creationDate.getTime();
        Double interest = calcularInteres(fixedTermRequest.getAmount(),
                totalMsBetweenDates);

        // Crear el plazo fijo
        FixedTermDeposit fixedTermDeposit = FixedTermDeposit.builder()
                .amount(fixedTermRequest.getAmount())
                .account(account)
                .interest(interest)
                .creationDate(new Date())
                .closingDate(closingDate)
                .build();

        return FixedTermDepositResponse.builder().
                fixedTermDeposit(fixedTermDeposit).
                build();

    }

    private Date calculateClosingDate(Date creationDate, int totalDays){
        // Calcular la fecha de finalización en relación a la fecha de creación
        LocalDateTime creationLocalDateTime = creationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime closingLocalDateTime = creationLocalDateTime.plusDays(totalDays);
        return Date.from(closingLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Double calcularInteres (Double amount, long durationMs){
        double interest = interestPorcentage / 100;
        double dayInMs = 1000.0 * 60 * 60 * 24;
        double durationMsToDays = durationMs / dayInMs;

        //cantidad x interes x días
        return amount * interest * durationMsToDays;
    }

    public Account findArsAccount(User user) {
        List<Account> accounts = accountRepository.findByUser(user);
        return accounts.stream()
                .filter(c -> c.getCurrency() == Currency.ARS && !c.isSoftDelete())
                .findAny()
                .orElseThrow(() -> new AccountNotFoundException("El usuario no tiene una cuenta en pesos", Currency.ARS));
    }

}
