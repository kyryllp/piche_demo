package com.account.manager.demo.dto.account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateAccountRequestDto {

    @Schema(description = "Account balance in cents", example = "1000")
    @NotNull
    private Long balance;

    @Schema(description = "Account name", example = "John Doe")
    @NotBlank
    private String name;
}
