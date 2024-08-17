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
public class TransferRequestDto {

    @NotNull
    @PositiveOrZero
    @Schema(description = "Amount in cents", example = "1000")
    private Long amount;

    @NotNull
    @Schema(description = "From account id", example = "1")
    private Long fromAccountId;

    @NotNull
    @Schema(description = "To account id", example = "2")
    private Long toAccountId;
}
