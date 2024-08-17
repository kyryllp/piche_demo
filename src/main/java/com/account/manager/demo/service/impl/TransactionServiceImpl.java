package com.account.manager.demo.service.impl;

import com.account.manager.demo.dto.transaction.TransferRequestDto;
import com.account.manager.demo.dto.transaction.DepositRequestDto;
import com.account.manager.demo.dto.transaction.TransactionResponseDto;
import com.account.manager.demo.dto.transaction.WithdrawalRequestDto;
import com.account.manager.demo.entity.AccountEntity;
import com.account.manager.demo.entity.TransactionEntity;
import com.account.manager.demo.enums.TransactionType;
import com.account.manager.demo.exception.AccountNotFoundException;
import com.account.manager.demo.exception.IllegalBalanceException;
import com.account.manager.demo.mapper.TransactionMapper;
import com.account.manager.demo.repository.AccountRepository;
import com.account.manager.demo.repository.TransactionRepository;
import com.account.manager.demo.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    @Override
    public TransactionResponseDto deposit(DepositRequestDto dto) {
        AccountEntity account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(dto.getAccountId()));
        account.setBalance(account.getBalance() + dto.getAmount());
        accountRepository.save(account);

        TransactionEntity transaction = TransactionEntity.builder()
                .amount(dto.getAmount())
                .account(account)
                .type(TransactionType.DEPOSIT)
                .createdAt(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);

        return transactionMapper.toDto(transaction);
    }

    @Transactional
    @Override
    public TransactionResponseDto withdrawal(WithdrawalRequestDto dto) {
        AccountEntity account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(dto.getAccountId()));

        long newBalance = account.getBalance() - dto.getAmount();
        if (newBalance < 0) {
            throw new IllegalBalanceException("Withdrawal amount is greater than balance");
        }
        account.setBalance(newBalance);
        accountRepository.save(account);

        TransactionEntity transaction = TransactionEntity.builder()
                .amount(dto.getAmount())
                .account(account)
                .type(TransactionType.WITHDRAWAL)
                .createdAt(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);

        return transactionMapper.toDto(transaction);
    }

    @Transactional
    @Override
    public TransactionResponseDto transfer(TransferRequestDto dto) {
        AccountEntity fromAccount = accountRepository.findById(dto.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(dto.getFromAccountId()));
        AccountEntity toAccount = accountRepository.findById(dto.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException(dto.getToAccountId()));

        long newFromBalance = fromAccount.getBalance() - dto.getAmount();
        if (newFromBalance < 0) {
            throw new IllegalBalanceException("Transfer amount is greater than balance");
        }
        fromAccount.setBalance(newFromBalance);
        accountRepository.save(fromAccount);

        long newToBalance = toAccount.getBalance() + dto.getAmount();
        toAccount.setBalance(newToBalance);
        accountRepository.save(toAccount);

        TransactionEntity transferOutTransaction = TransactionEntity.builder()
                .amount(dto.getAmount())
                .account(fromAccount)
                .type(TransactionType.TRANSFER_OUT)
                .createdAt(LocalDateTime.now())
                .build();
        TransactionEntity transferInTransaction
                = TransactionEntity.builder()
                .amount(dto.getAmount())
                .account(fromAccount)
                .type(TransactionType.TRANSFER_IN)
                .createdAt(LocalDateTime.now())
                .build();
        transactionRepository.save(transferOutTransaction);
        transactionRepository.save(transferInTransaction);

        return transactionMapper.toDto(transferOutTransaction);
    }
}
