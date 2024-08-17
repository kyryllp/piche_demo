package com.account.manager.demo.service;

import com.account.manager.demo.dto.account.CreateAccountRequestDto;
import com.account.manager.demo.dto.account.AccountInfoResponseDto;
import com.account.manager.demo.entity.AccountEntity;
import com.account.manager.demo.exception.AccountNotFoundException;
import com.account.manager.demo.mapper.AccountMapper;
import com.account.manager.demo.repository.AccountRepository;
import com.account.manager.demo.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountEntity testAccount;
    private AccountInfoResponseDto testAccountResponseDto;

    @BeforeEach
    void setUp() {
        testAccount = AccountEntity.builder()
                .id(1L)
                .balance(1000L)
                .build();

        testAccountResponseDto = AccountInfoResponseDto.builder()
                .id(1L)
                .balance(1000L)
                .build();
    }

    @Test
    void findAll_shouldReturnListOfAccountInfoResponseDto() {
        when(accountRepository.findAll()).thenReturn(List.of(testAccount));
        when(accountMapper.toDto(testAccount)).thenReturn(testAccountResponseDto);

        List<AccountInfoResponseDto> result = accountService.findAll();

        assertEquals(1, result.size());
        assertEquals(testAccountResponseDto, result.get(0));
        verify(accountRepository).findAll();
        verify(accountMapper).toDto(testAccount);
    }

    @Test
    void findById_shouldReturnAccountInfoResponseDto() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountMapper.toDto(testAccount)).thenReturn(testAccountResponseDto);

        AccountInfoResponseDto result = accountService.findById(1L);

        assertEquals(testAccountResponseDto, result);
        verify(accountRepository).findById(1L);
        verify(accountMapper).toDto(testAccount);
    }

    @Test
    void findById_shouldThrowAccountNotFoundException() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.findById(1L));
        verify(accountRepository).findById(1L);
        verify(accountMapper, never()).toDto(any(AccountEntity.class));
    }

    @Test
    void create_shouldSaveAccountAndReturnAccountInfoResponseDto() {
        CreateAccountRequestDto createAccountRequestDto = CreateAccountRequestDto.builder()
                .balance(1000L)
                .build();

        when(accountMapper.toEntity(createAccountRequestDto)).thenReturn(testAccount);
        when(accountRepository.save(testAccount)).thenReturn(testAccount);
        when(accountMapper.toDto(testAccount)).thenReturn(testAccountResponseDto);

        AccountInfoResponseDto result = accountService.create(createAccountRequestDto);

        assertEquals(testAccountResponseDto, result);
        verify(accountRepository).save(testAccount);
        verify(accountMapper).toDto(testAccount);
    }
}
