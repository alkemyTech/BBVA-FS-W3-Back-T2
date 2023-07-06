package com.bbva.wallet.services;

import com.bbva.wallet.dtos.JwtAuthenticationResponse;
import com.bbva.wallet.dtos.RegisterRequest;
import com.bbva.wallet.dtos.SignInRequest;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final AccountService accountService;
    private final UserService userService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;

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

    public JwtAuthenticationResponse signIn(SignInRequest request) throws AuthenticationException {
        String email = request.getEmail();
        String password = request.getPassword();
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException ex) {
            throw new AuthenticationException("Correo electrónico o contraseña inválida");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    private boolean validateEmail(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

}
