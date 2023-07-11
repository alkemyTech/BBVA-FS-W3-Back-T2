package com.bbva.wallet.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNotFoundException extends RuntimeException{

    private Long userId;
    public UserNotFoundException(String message, Long id) {
        super(message);
        this.userId = id;
    }

}
