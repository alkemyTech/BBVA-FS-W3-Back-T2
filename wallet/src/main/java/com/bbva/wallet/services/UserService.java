package com.bbva.wallet.services;

import com.bbva.wallet.dtos.PagedUserResponse;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exceptions.InvalidUrlRequestException;
import com.bbva.wallet.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.bbva.wallet.dtos.RegisterRequest;
import com.bbva.wallet.entities.Role;
import com.bbva.wallet.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
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

    public com.bbva.wallet.dtos.PagedUserResponse findAllUsers(int page) {
        int pageSize = 2;
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

}


