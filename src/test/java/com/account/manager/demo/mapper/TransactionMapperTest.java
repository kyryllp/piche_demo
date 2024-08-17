package com.account.manager.demo.mapper;

import com.account.manager.demo.dto.transaction.TransactionResponseDto;
import com.account.manager.demo.entity.TransactionEntity;
import com.account.manager.demo.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    private TransactionMapper transactionMapper;

    @BeforeEach
    void setUp() {
        transactionMapper = new TransactionMapper();
    }

    @Test
    void toDto_shouldMapAllFieldsCorrectly() {
        Long id = 1L;
        Long amount = 100L;
        TransactionType type = TransactionType.DEPOSIT;
        LocalDateTime createdAt = LocalDateTime.now();

        TransactionEntity entity = TransactionEntity.builder()
                .id(id)
                .amount(amount)
                .type(type)
                .createdAt(createdAt)
                .build();

        TransactionResponseDto dto = transactionMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(amount, dto.getAmount());
        assertEquals(type, dto.getType());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void toDto_shouldHandleNullEntity() {
        assertThrows(NullPointerException.class, () -> transactionMapper.toDto(null),
                "Should throw NullPointerException for null entity");
    }

    @Test
    void toDto_shouldHandleNullFields() {
        TransactionEntity entity = TransactionEntity.builder().build();

        TransactionResponseDto dto = transactionMapper.toDto(entity);

        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getAmount());
        assertNull(dto.getType());
        assertNull(dto.getCreatedAt());
    }
}