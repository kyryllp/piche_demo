package com.account.manager.demo.controller;

import com.account.manager.demo.dto.account.AccountInfoResponseDto;
import com.account.manager.demo.dto.account.CreateAccountRequestDto;
import com.account.manager.demo.entity.AccountEntity;
import com.account.manager.demo.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AccountControllerV1Test {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void findAll_shouldReturnListOfAccounts() {
        AccountEntity entity = AccountEntity.builder()
                .balance(1000L)
                .name("Test Account")
                .createdAt(LocalDateTime.now())
                .build();
        accountRepository.save(entity);

        ResponseEntity<AccountInfoResponseDto[]> response = restTemplate.getForEntity("/v1/accounts", AccountInfoResponseDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).hasAtLeastOneElementOfType(AccountInfoResponseDto.class);

        accountRepository.delete(entity);
    }

    @Test
    void getAccount_shouldReturnAccountById() {
        AccountEntity savedAccount = accountRepository.save(AccountEntity.builder()
                .balance(1000L)
                .name("Test Account")
                .createdAt(LocalDateTime.now())
                .build());
        accountRepository.save(savedAccount);

        String url = UriComponentsBuilder.fromPath("/v1/accounts/{id}")
                .buildAndExpand(savedAccount.getId())
                .toUriString();

        ResponseEntity<AccountInfoResponseDto> response = restTemplate.getForEntity(url, AccountInfoResponseDto.class);
        accountRepository.delete(savedAccount);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(savedAccount.getId());
        assertThat(response.getBody().getBalance()).isEqualTo(savedAccount.getBalance());
    }

    @Test
    void getAccount_shouldReturn404WhenNotFound() {
        String url = UriComponentsBuilder.fromPath("/v1/accounts/{id}")
                .buildAndExpand(999L)
                .toUriString();

        ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void createAccount_shouldCreateNewAccount() {
        CreateAccountRequestDto createAccountRequestDto = CreateAccountRequestDto.builder()
                .name("New Account")
                .balance(1500L)
                .build();

        ResponseEntity<AccountInfoResponseDto> response = restTemplate.postForEntity("/v1/accounts", createAccountRequestDto, AccountInfoResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getBalance()).isEqualTo(1500L);

        AccountEntity createdAccount = accountRepository.findById(response.getBody().getId()).orElse(null);
        accountRepository.delete(createdAccount);

        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getBalance()).isEqualTo(1500L);

    }

    @Test
    void createAccount_shouldReturn400ForInvalidRequest() {
        CreateAccountRequestDto createAccountRequestDto = CreateAccountRequestDto.builder()
                .balance(-1500L)
                .build();

        ResponseEntity<Void> response = restTemplate.postForEntity("/v1/accounts", createAccountRequestDto, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }
}
