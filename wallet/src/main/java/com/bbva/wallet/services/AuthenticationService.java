package com.bbva.wallet.services;

import com.bbva.wallet.dtos.RegisterRequest;
import com.bbva.wallet.entities.Role;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.exceptions.DuplicateEmailException;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.utils.CbuUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final UserService userService;
    private final RoleService roleService;

    public User signUp(@Valid RegisterRequest request) {
        if (validateEmail(request.getEmail())) {
            Role userRole = roleService.getOrCreateUserRole(RoleName.USER);
            User user = userService.buildUser(request, userRole);

            String cbuArsAccount = CbuUtil.generateCbu();
            String cbuUsdAccount = CbuUtil.generateCbu();

            while (accountRepository.findById(cbuArsAccount).isPresent()) {
                cbuArsAccount = CbuUtil.generateCbu();
            }

            while (accountRepository.findById(cbuUsdAccount).isPresent()) {
                cbuUsdAccount = CbuUtil.generateCbu();
            }

            Account arsAccount = accountService.buildAccount(cbuArsAccount, Currency.ARS, 300_000, user);
            Account usdAccount = accountService.buildAccount(cbuUsdAccount, Currency.USD, 1_000, user);

            userRepository.save(user);
            accountRepository.saveAll(List.of(arsAccount, usdAccount));

            return user;
        }

        throw new DuplicateEmailException("El correo electrónico ya está registrado", request.getEmail());
    }

    private boolean validateEmail(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

}
