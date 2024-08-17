package com.account.manager.demo.dto.transaction;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WithdrawalRequestDto {

    @NotNull
    @PositiveOrZero
    @Schema(description = "Amount in cents", example = "1000")
    private Long amount;

    @NotNull
    @Schema(description = "Account id", example = "1")
    private Long accountId;
}
