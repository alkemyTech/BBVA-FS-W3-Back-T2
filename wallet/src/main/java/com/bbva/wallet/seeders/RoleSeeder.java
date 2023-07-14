package com.bbva.wallet.seeders;

import com.bbva.wallet.entities.Role;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleSeeder implements CommandLineRunner, Ordered {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        loadRoleData();
    }

    private void loadRoleData() {
        if (roleRepository.count() == 0) {
            Role userRole = Role.builder().name(RoleName.USER).description("User").build();
            Role adminRole = Role.builder().name(RoleName.ADMIN).description("Admin").build();
            roleRepository.saveAll(List.of(userRole, adminRole));
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
