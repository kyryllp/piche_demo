package com.account.manager.demo.controller;

import com.account.manager.demo.dto.transaction.DepositRequestDto;
import com.account.manager.demo.dto.transaction.TransferRequestDto;
import com.account.manager.demo.dto.transaction.TransactionResponseDto;
import com.account.manager.demo.dto.transaction.WithdrawalRequestDto;
import com.account.manager.demo.entity.AccountEntity;
import com.account.manager.demo.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TransactionControllerV1Test {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void deposit_shouldIncreaseAccountBalance() {
        AccountEntity accountFrom = createTestAccount(1000L, "Account From");

        DepositRequestDto depositRequestDto = DepositRequestDto.builder()
                .accountId(accountFrom.getId())
                .amount(500L)
                .build();

        ResponseEntity<TransactionResponseDto> response = restTemplate.postForEntity("/v1/transactions/deposit", depositRequestDto, TransactionResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAmount()).isEqualTo(500L);

        AccountEntity updatedAccount = accountRepository.findById(accountFrom.getId()).orElse(null);
        assertThat(updatedAccount).isNotNull();
        assertThat(updatedAccount.getBalance()).isEqualTo(1500L);

        accountRepository.deleteById(accountFrom.getId());
    }

    @Test
    void withdrawal_shouldDecreaseAccountBalance() {
        AccountEntity accountFrom = createTestAccount(1000L, "Account From");

        WithdrawalRequestDto withdrawalRequestDto = WithdrawalRequestDto.builder()
                .accountId(accountFrom.getId())
                .amount(500L)
                .build();

        ResponseEntity<TransactionResponseDto> response = restTemplate.postForEntity("/v1/transactions/withdrawal", withdrawalRequestDto, TransactionResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAmount()).isEqualTo(500L);

        AccountEntity updatedAccount = accountRepository.findById(accountFrom.getId()).orElse(null);
        assertThat(updatedAccount).isNotNull();
        assertThat(updatedAccount.getBalance()).isEqualTo(500L);

        accountRepository.deleteById(accountFrom.getId());
    }

    @Test
    void withdrawal_shouldReturn400WhenInsufficientBalance() {
        AccountEntity accountFrom = createTestAccount(1000L, "Account From");

        WithdrawalRequestDto withdrawalRequestDto = WithdrawalRequestDto.builder()
                .accountId(accountFrom.getId())
                .amount(1500L)
                .build();

        ResponseEntity<Void> response = restTemplate.postForEntity("/v1/transactions/withdrawal", withdrawalRequestDto, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);

        accountRepository.deleteById(accountFrom.getId());
    }

    @Test
    void transfer_shouldMoveMoneyBetweenAccounts() {
        AccountEntity accountFrom = createTestAccount(1000L, "Account From");
        AccountEntity accountTo = createTestAccount(500L, "Account To");

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .fromAccountId(accountFrom.getId())
                .toAccountId(accountTo.getId())
                .amount(500L)
                .build();

        ResponseEntity<TransactionResponseDto> response = restTemplate.postForEntity("/v1/transactions/transfer", transferRequestDto, TransactionResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAmount()).isEqualTo(500L);

        AccountEntity updatedFromAccount = accountRepository.findById(accountFrom.getId()).orElse(null);
        AccountEntity updatedToAccount = accountRepository.findById(accountTo.getId()).orElse(null);

        assertThat(updatedFromAccount).isNotNull();
        assertThat(updatedFromAccount.getBalance()).isEqualTo(500L);

        assertThat(updatedToAccount).isNotNull();
        assertThat(updatedToAccount.getBalance()).isEqualTo(1000L);

        accountRepository.deleteById(accountFrom.getId());
        accountRepository.deleteById(accountTo.getId());
    }

    @Test
    void transfer_shouldReturn400WhenInsufficientBalance() {
        AccountEntity accountFrom = createTestAccount(1000L, "Account From");
        AccountEntity accountTo = createTestAccount(500L, "Account To");

        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .fromAccountId(accountFrom.getId())
                .toAccountId(accountTo.getId())
                .amount(1500L)
                .build();

        ResponseEntity<Void> response = restTemplate.postForEntity("/v1/transactions/transfer", transferRequestDto, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);

        accountRepository.deleteById(accountFrom.getId());
        accountRepository.deleteById(accountTo.getId());
    }

    private AccountEntity createTestAccount(long balance, String name) {
        return accountRepository.save(AccountEntity.builder()
                .balance(balance)
                .name(name)
                .createdAt(LocalDateTime.now())
                .build());
    }
}
