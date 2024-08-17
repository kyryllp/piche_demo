package com.account.manager.demo.mapper;

import com.account.manager.demo.dto.transaction.TransactionResponseDto;
import com.account.manager.demo.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponseDto toDto(TransactionEntity entity) {
        return TransactionResponseDto.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
