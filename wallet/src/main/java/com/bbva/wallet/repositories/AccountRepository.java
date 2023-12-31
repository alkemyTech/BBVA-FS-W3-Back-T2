package com.bbva.wallet.repositories;

import com.bbva.wallet.dtos.CbuAuthenticateRequest;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByUserAndCurrency(User user, Currency currency);
    List<Account> findByUser(User user);
    Optional<Account> findByUserAndCurrency(User user, Currency currency);
    Page<Account> findAll(Pageable pageable);

    Account findByCbu(String cbu);
}
