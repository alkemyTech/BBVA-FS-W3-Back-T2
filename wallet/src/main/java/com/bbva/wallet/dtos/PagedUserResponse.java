package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.User;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PagedUserResponse {
    private List<User> users;
    private String previousPageUrl;
    private String nextPageUrl;
}
