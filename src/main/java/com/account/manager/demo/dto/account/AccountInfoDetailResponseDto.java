package com.account.manager.demo.dto.account;

import com.account.manager.demo.dto.transaction.TransactionResponseDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountInfoDetailResponseDto {

    private Long id;

    private Long balance;

    private String name;

    private LocalDateTime createdAt;

    private List<TransactionResponseDto> transactions;
}
