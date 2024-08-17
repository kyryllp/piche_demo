package com.account.manager.demo.service;

import com.account.manager.demo.dto.transaction.*;
import com.account.manager.demo.entity.AccountEntity;
import com.account.manager.demo.entity.TransactionEntity;
import com.account.manager.demo.enums.TransactionType;
import com.account.manager.demo.exception.AccountNotFoundException;
import com.account.manager.demo.exception.IllegalBalanceException;
import com.account.manager.demo.mapper.TransactionMapper;
import com.account.manager.demo.repository.AccountRepository;
import com.account.manager.demo.repository.TransactionRepository;
import com.account.manager.demo.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private AccountEntity testAccount;
    private TransactionEntity testTransaction;
    private TransactionResponseDto testTransactionResponseDto;

    @BeforeEach
    void setUp() {
        testAccount = AccountEntity.builder()
                .id(1L)
                .balance(1000L)
                .build();

        testTransaction = TransactionEntity.builder()
                .id(1L)
                .amount(100L)
                .type(TransactionType.DEPOSIT)
                .account(testAccount)
                .createdAt(LocalDateTime.now())
                .build();

        testTransactionResponseDto = TransactionResponseDto.builder()
                .id(1L)
                .amount(100L)
                .type(TransactionType.DEPOSIT)
                .createdAt(testTransaction.getCreatedAt())
                .build();
    }

    @Test
    void deposit_shouldCreateTransactionAndUpdateBalance() {
        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .accountId(1L)
                .amount(100L)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(testTransaction);
        when(transactionMapper.toDto(any(TransactionEntity.class))).thenReturn(testTransactionResponseDto);

        TransactionResponseDto result = transactionService.deposit(depositRequestDto);

        assertEquals(1100L, testAccount.getBalance());
        assertEquals(testTransactionResponseDto, result);
        verify(accountRepository).save(testAccount);
        verify(transactionRepository).save(any(TransactionEntity.class));
    }

    @Test
    void deposit_shouldThrowAccountNotFoundException() {
        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .accountId(1L)
                .amount(100L)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transactionService.deposit(depositRequestDto));
    }

    @Test
    void withdrawal_shouldCreateTransactionAndUpdateBalance() {
        WithdrawalRequestDto withdrawalRequestDto = WithdrawalRequestDto.builder()
                .accountId(1L)
                .amount(100L)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(testTransaction);
        when(transactionMapper.toDto(any(TransactionEntity.class))).thenReturn(testTransactionResponseDto);

        TransactionResponseDto result = transactionService.withdrawal(withdrawalRequestDto);

        assertEquals(900L, testAccount.getBalance());
        assertEquals(testTransactionResponseDto, result);
        verify(accountRepository).save(testAccount);
        verify(transactionRepository).save(any(TransactionEntity.class));
    }

    @Test
    void withdrawal_shouldThrowIllegalBalanceException() {
        WithdrawalRequestDto withdrawalRequestDto = WithdrawalRequestDto.builder()
                .amount(1100L)
                .accountId(1L)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        assertThrows(IllegalBalanceException.class, () -> transactionService.withdrawal(withdrawalRequestDto));
    }

    @Test
    void transfer_shouldCreateTransactionsAndUpdateBalances() {
        AccountEntity toAccount = AccountEntity.builder()
                .id(2L)
                .balance(500L)
                .build();

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .amount(100L)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(testTransaction);
        when(transactionMapper.toDto(any(TransactionEntity.class))).thenReturn(testTransactionResponseDto);

        TransactionResponseDto result = transactionService.transfer(transferRequestDto);

        assertEquals(900L, testAccount.getBalance());
        assertEquals(600L, toAccount.getBalance());
        assertEquals(testTransactionResponseDto, result);
        verify(accountRepository, times(2)).save(any(AccountEntity.class));
        verify(transactionRepository, times(2)).save(any(TransactionEntity.class));
    }

    @Test
    void transfer_shouldThrowIllegalBalanceException() {
        AccountEntity toAccount = AccountEntity.builder()
                .id(2L)
                .balance(500L)
                .build();

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .amount(1100L)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        assertThrows(IllegalBalanceException.class, () -> transactionService.transfer(transferRequestDto));
    }
}
