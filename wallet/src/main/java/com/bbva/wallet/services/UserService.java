package com.bbva.wallet.services;

import com.bbva.wallet.dtos.PagedUserResponse;
import com.bbva.wallet.dtos.RegisterRequest;
import com.bbva.wallet.dtos.UpdateUserRequest;
import com.bbva.wallet.entities.Role;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.InvalidUrlRequestException;
import com.bbva.wallet.exceptions.UserNotFoundException;
import com.bbva.wallet.repositories.RoleRepository;
import com.bbva.wallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public PagedUserResponse findAllUsers(int page) {
        int pageSize = 10;
        PagedUserResponse pagedUserResponse = new PagedUserResponse();
        Pageable pageable = PageRequest.of(page, pageSize);

        //contenido de página
        Page<User> userPage = userRepository.findAll(pageable);
        pagedUserResponse.setUsers(userPage.getContent());

        //valida que la página tenga contenido
        if(pagedUserResponse.getUsers().size() == 0)
            throw new InvalidUrlRequestException("La página buscada no se encuentra disponible.");

        //url de página siguiente
        if (userPage.hasNext()) {
            int nextPage = userPage.getNumber() + 1;
            String nextPageUrl = "/users?page=" + nextPage;
            pagedUserResponse.setNextPageUrl(nextPageUrl);
        }

        //url de página anterior
        if (userPage.hasPrevious()) {
            int previousPage = userPage.getNumber() - 1;
            String previousPageUrl = "/users?page=" + previousPage;
            pagedUserResponse.setPreviousPageUrl(previousPageUrl);
        }

        return pagedUserResponse;
    }

    public User updateUser(Long id, UpdateUserRequest updateUserRequest) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User updatedUser = userOptional.get();
            if (!updateUserRequest.getFirstName().isEmpty()) {
                updatedUser.setFirstName(updateUserRequest.getFirstName());
            }
            if (!updateUserRequest.getLastName().isEmpty()) {
                updatedUser.setLastName(updateUserRequest.getLastName());
            }
            if (!updateUserRequest.getPassword().isEmpty()) {
                updatedUser.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
            }
            return userRepository.save(updatedUser);
        } else {
            throw new UserNotFoundException("No se ha encontrado al usuario", id);
        }
    }

    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent())
            return user.get();
        throw new UserNotFoundException("No se ha encontrado al usuario", id);
    }
}


