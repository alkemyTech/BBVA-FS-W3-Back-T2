package com.bbva.wallet.services;

import com.bbva.wallet.dtos.UpdateUser;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.UserNotFoundException;
import com.bbva.wallet.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.bbva.wallet.dtos.RegisterRequest;
import com.bbva.wallet.entities.Role;
import com.bbva.wallet.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public User buildUser(RegisterRequest request, Role userRole) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleId(userRole)
                .creationDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .softDelete(false)
                .build();
    }

    public User userSoftDelete(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setSoftDelete(true);
            userRepository.save(user);

            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el usuario");
        }
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, UpdateUser updateUser) {
        Optional<User> userOptional  = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User updatedUser = userOptional.get();
            if (!updateUser.getFirstName().isEmpty()) {
                updatedUser.setFirstName(updateUser.getFirstName());
            }
            if (!updateUser.getLastName().isEmpty()) {
                updatedUser.setLastName(updateUser.getLastName());
            }
            if (!updatedUser.getPassword().isEmpty()) {
                updatedUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
            }
            return userRepository.save(updatedUser);
        } else {
            throw new UserNotFoundException("No se ha encontrado al usuario", id);
        }
    }
}
