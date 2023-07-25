package com.bbva.wallet.dtos;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageAccountResponse {
    private List<AccountDto> accounts;
    private String previousPageUrl;
    private String nextPageUrl;
}