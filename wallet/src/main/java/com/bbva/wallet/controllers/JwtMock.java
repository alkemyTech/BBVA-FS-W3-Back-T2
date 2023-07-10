package com.bbva.wallet.controllers;

import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwtmock")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class JwtMock {

    private UserRepository userRepository;

    @GetMapping()
    public String GetJWT(@RequestParam("user_id") Long userID) {
        var user  = userRepository.findById(userID);
        return JwtUtil.generateToken(user.get());
    }

}
