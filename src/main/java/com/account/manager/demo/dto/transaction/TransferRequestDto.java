package com.account.manager.demo.dto.transaction;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransferRequestDto {

    @NotNull
    @PositiveOrZero
    private Long amount;

    @NotNull
    private Long fromAccountId;

    @NotNull
    private Long toAccountId;
}
