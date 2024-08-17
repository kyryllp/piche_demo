package com.account.manager.demo.dto.transaction;

import com.account.manager.demo.enums.TransactionType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionResponseDto {

    private Long id;

    private Long amount;

    private TransactionType type;

    private LocalDateTime createdAt;
}
