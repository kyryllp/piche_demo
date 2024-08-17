package com.account.manager.demo.mapper;

import com.account.manager.demo.dto.account.AccountInfoDetailResponseDto;
import com.account.manager.demo.dto.account.AccountInfoResponseDto;
import com.account.manager.demo.dto.account.CreateAccountRequestDto;
import com.account.manager.demo.dto.transaction.TransactionResponseDto;
import com.account.manager.demo.entity.AccountEntity;
import com.account.manager.demo.entity.TransactionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountMapperTest {

    @Mock
    private TransactionMapper transactionMapper;

    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        accountMapper = new AccountMapper(transactionMapper);
    }

    @Test
    void toDto_shouldMapAccountEntityToAccountInfoResponseDto() {
        LocalDateTime now = LocalDateTime.now();
        AccountEntity entity = AccountEntity.builder()
                .id(1L)
                .balance(1000L)
                .name("Test Account")
                .createdAt(now)
                .build();

        AccountInfoResponseDto dto = accountMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1000L, dto.getBalance());
        assertEquals("Test Account", dto.getName());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    void toDetailDto_shouldMapAccountEntityToAccountInfoDetailResponseDto() {
        LocalDateTime now = LocalDateTime.now();
        TransactionEntity transactionEntity = TransactionEntity.builder().id(1L).build();
        AccountEntity entity = AccountEntity.builder()
                .id(1L)
                .balance(1000L)
                .name("Test Account")
                .createdAt(now)
                .transactions(List.of(transactionEntity))
                .build();

        TransactionResponseDto transactionDto = TransactionResponseDto.builder().id(1L).build();
        when(transactionMapper.toDto(transactionEntity)).thenReturn(transactionDto);

        AccountInfoDetailResponseDto dto = accountMapper.toDetailDto(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1000L, dto.getBalance());
        assertEquals("Test Account", dto.getName());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(1, dto.getTransactions().size());
        assertEquals(1L, dto.getTransactions().get(0).getId());

        verify(transactionMapper).toDto(transactionEntity);
    }

    @Test
    void toEntity_shouldMapCreateAccountRequestDtoToAccountEntity() {
        CreateAccountRequestDto dto = CreateAccountRequestDto.builder()
                .balance(1000L)
                .name("New Account")
                .build();

        AccountEntity entity = accountMapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals(1000L, entity.getBalance());
        assertEquals("New Account", entity.getName());
        assertNotNull(entity.getCreatedAt());
        assertTrue(entity.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(entity.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
    }
}