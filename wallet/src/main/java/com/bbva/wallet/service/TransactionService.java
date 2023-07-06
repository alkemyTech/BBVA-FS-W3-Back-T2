package com.bbva.wallet.service;

import com.bbva.wallet.dto.TransactionInputDto;

public interface TransactionService {


   boolean SendArs(Long SenderUserID,TransactionInputDto Receiver);

}
