package com.bbva.wallet.services;

import com.bbva.wallet.entities.Role;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.repositories.RoleRepository;
import com.bbva.wallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public Role getOrCreateUserRole(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> createNewUserRole(roleName));
    }

    public Role createNewUserRole(RoleName roleName) {
        Role userRole = Role.builder()
                .name(roleName)
                .description("User")
                .creationDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        return roleRepository.save(userRole);
    }
}
