package com.bbva.wallet.services;

import com.bbva.wallet.dtos.TransactionInputDto;

public interface TransactionService {


   boolean SendArs(Long SenderUserID,TransactionInputDto Receiver);
   boolean SendUsd(Long SenderUserID,TransactionInputDto Receiver);

}
