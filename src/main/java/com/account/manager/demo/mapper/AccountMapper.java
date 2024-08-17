package com.account.manager.demo.mapper;

import com.account.manager.demo.dto.account.AccountInfoDetailResponseDto;
import com.account.manager.demo.dto.account.AccountInfoResponseDto;
import com.account.manager.demo.dto.account.CreateAccountRequestDto;
import com.account.manager.demo.entity.AccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final TransactionMapper transactionMapper;

    public AccountInfoResponseDto toDto(AccountEntity entity) {

        return AccountInfoResponseDto.builder()
                .id(entity.getId())
                .balance(entity.getBalance())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public AccountInfoDetailResponseDto toDetailDto(AccountEntity entity) {

        return AccountInfoDetailResponseDto.builder()
                .id(entity.getId())
                .balance(entity.getBalance())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .transactions(entity.getTransactions().stream()
                        .map(transactionMapper::toDto)
                        .toList())
                .build();
    }

    public AccountEntity toEntity(CreateAccountRequestDto dto) {

        return AccountEntity.builder()
                .balance(dto.getBalance())
                .name(dto.getName())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
