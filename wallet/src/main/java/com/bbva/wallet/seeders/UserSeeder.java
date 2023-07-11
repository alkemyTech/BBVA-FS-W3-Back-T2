package com.bbva.wallet.seeders;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.repositories.RoleRepository;
import com.bbva.wallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserSeeder implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadUserData();
    }

    private void loadUserData() {
        if (userRepository.count() == 0) {
            User user = User.builder()
                    .firstName("Rodrigo")
                    .lastName("Juarez")
                    .email("rodrigo@gmail.com")
                    .password(passwordEncoder.encode("pass-rodrigo"))
                    .roleId(roleRepository.findByName(RoleName.USER).orElse(null))
                    .build();
            User user2 = User.builder()
                    .firstName("Renata")
                    .lastName("Juarez")
                    .email("renata@gmail.com")
                    .password(passwordEncoder.encode("pass-renata"))
                    .roleId(roleRepository.findByName(RoleName.USER).orElse(null))
                    .build();
            User user3 = User.builder()
                    .firstName("Omar")
                    .lastName("Juarez")
                    .email("omar@gmail.com")
                    .password(passwordEncoder.encode("pass-omar"))
                    .roleId(roleRepository.findByName(RoleName.USER).orElse(null))
                    .build();
            User user4 = User.builder()
                    .firstName("Marina")
                    .lastName("Calandra")
                    .email("marina@gmail.com")
                    .password(passwordEncoder.encode("pass-marina"))
                    .roleId(roleRepository.findByName(RoleName.USER).orElse(null))
                    .build();
            User user5 = User.builder()
                    .firstName("Simón")
                    .lastName("Neiva")
                    .email("simon@gmail.com")
                    .password(passwordEncoder.encode("pass-simon"))
                    .roleId(roleRepository.findByName(RoleName.USER).orElse(null))
                    .build();
            User user6 = User.builder()
                    .firstName("Iara")
                    .lastName("Pou")
                    .email("iara@gmail.com")
                    .password(passwordEncoder.encode("pass-iara"))
                    .roleId(roleRepository.findByName(RoleName.USER).orElse(null))
                    .build();
            User user7 = User.builder()
                    .firstName("Valentino")
                    .lastName("Veralli")
                    .email("valentino@gmail.com")
                    .password(passwordEncoder.encode("pass-valentino"))
                    .roleId(roleRepository.findByName(RoleName.USER).orElse(null))
                    .build();
            User user8 = User.builder()
                    .firstName("Carmen")
                    .lastName("Juarez")
                    .email("carmen@gmail.com")
                    .password(passwordEncoder.encode("pass-carmen"))
                    .roleId(roleRepository.findByName(RoleName.USER).orElse(null))
                    .build();
            User user9 = User.builder()
                    .firstName("Irma")
                    .lastName("Lauletta")
                    .email("irma@gmail.com")
                    .password(passwordEncoder.encode("pass-irma"))
                    .roleId(roleRepository.findByName(RoleName.USER).orElse(null))
                    .build();
            User user10 = User.builder()
                    .firstName("Yamil")
                    .lastName("Ventura")
                    .email("yamil@gmail.com")
                    .password(passwordEncoder.encode("pass-yamil"))
                    .roleId(roleRepository.findByName(RoleName.USER).orElse(null))
                    .build();
            User user11 = User.builder()
                    .firstName("Juan")
                    .lastName("Pérez")
                    .email("juan@gmail.com")
                    .password(passwordEncoder.encode("pass-juan"))
                    .roleId(roleRepository.findByName(RoleName.ADMIN).orElse(null))
                    .build();
            User user12 = User.builder()
                    .firstName("María")
                    .lastName("González")
                    .email("maria@gmail.com")
                    .password(passwordEncoder.encode("pass-maria"))
                    .roleId(roleRepository.findByName(RoleName.ADMIN).orElse(null))
                    .build();
            User user13 = User.builder()
                    .firstName("Carlos")
                    .lastName("López")
                    .email("carlos@gmail.com")
                    .password(passwordEncoder.encode("pass-carlos"))
                    .roleId(roleRepository.findByName(RoleName.ADMIN).orElse(null))
                    .build();
            User user14 = User.builder()
                    .firstName("Ana")
                    .lastName("Sánchez")
                    .email("ana@gmail.com")
                    .password(passwordEncoder.encode("pass-ana"))
                    .roleId(roleRepository.findByName(RoleName.ADMIN).orElse(null))
                    .build();
            User user15 = User.builder()
                    .firstName("Pedro")
                    .lastName("Martínez")
                    .email("pedro@gmail.com")
                    .password(passwordEncoder.encode("pass-pedro"))
                    .roleId(roleRepository.findByName(RoleName.ADMIN).orElse(null))
                    .build();
            User user16 = User.builder()
                    .firstName("Laura")
                    .lastName("Torres")
                    .email("laura@gmail.com")
                    .password(passwordEncoder.encode("pass-laura"))
                    .roleId(roleRepository.findByName(RoleName.ADMIN).orElse(null))
                    .build();
            User user17 = User.builder()
                    .firstName("José")
                    .lastName("Rodríguez")
                    .email("jose@gmail.com")
                    .password(passwordEncoder.encode("pass-jose"))
                    .roleId(roleRepository.findByName(RoleName.ADMIN).orElse(null))
                    .build();
            User user18 = User.builder()
                    .firstName("Luis")
                    .lastName("Hernández")
                    .email("luis@gmail.com")
                    .password(passwordEncoder.encode("pass-luis"))
                    .roleId(roleRepository.findByName(RoleName.ADMIN).orElse(null))
                    .build();
            User user19 = User.builder()
                    .firstName("Marta")
                    .lastName("López")
                    .email("marta@gmail.com")
                    .password(passwordEncoder.encode("pass-marta"))
                    .roleId(roleRepository.findByName(RoleName.ADMIN).orElse(null))
                    .build();
            User user20 = User.builder()
                    .firstName("Javier")
                    .lastName("Fernández")
                    .email("javier@gmail.com")
                    .password(passwordEncoder.encode("pass-javier"))
                    .roleId(roleRepository.findByName(RoleName.ADMIN).orElse(null))
                    .build();
            userRepository.saveAll(List.of(user, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18, user19, user20));
        }
        System.out.println(userRepository.count());
    }
}
