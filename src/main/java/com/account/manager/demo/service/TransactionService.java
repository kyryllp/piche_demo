package com.account.manager.demo.service;

import com.account.manager.demo.dto.transaction.TransferRequestDto;
import com.account.manager.demo.dto.transaction.DepositRequestDto;
import com.account.manager.demo.dto.transaction.TransactionResponseDto;
import com.account.manager.demo.dto.transaction.WithdrawalRequestDto;

public interface TransactionService {

    TransactionResponseDto deposit(DepositRequestDto dto);

    TransactionResponseDto withdrawal(WithdrawalRequestDto dto);

    TransactionResponseDto transfer(TransferRequestDto dto);
}
