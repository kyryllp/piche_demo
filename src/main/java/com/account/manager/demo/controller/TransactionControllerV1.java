package com.account.manager.demo.controller;

import com.account.manager.demo.dto.transaction.TransferRequestDto;
import com.account.manager.demo.dto.transaction.DepositRequestDto;
import com.account.manager.demo.dto.transaction.TransactionResponseDto;
import com.account.manager.demo.dto.transaction.WithdrawalRequestDto;
import com.account.manager.demo.service.TransactionService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@OpenAPIDefinition(info = @Info(title = "Transactions API", version = "1.0", description = "Actions with transactions"))
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
public class TransactionControllerV1 {

    private final TransactionService transactionService;

    @Operation(
            operationId = "deposit",
            description = "Deposit money to account",
            responses = {
                    @ApiResponse(
                            content = @Content(
                                    schema = @Schema(implementation = TransactionResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "System error", content = @Content())
            }
    )
    @PostMapping("/deposit")
    public TransactionResponseDto deposit(@Valid @RequestBody DepositRequestDto dto) {
        log.trace("Depositing money to account with id: {}", dto.getAccountId());
        return transactionService.deposit(dto);
    }

    @Operation(
            operationId = "withdrawal",
            description = "Withdraw money from account",
            responses = {
                    @ApiResponse(
                            content = @Content(
                                    schema = @Schema(implementation = TransactionResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "System error", content = @Content())
            }
    )
    @PostMapping("/withdrawal")
    public TransactionResponseDto withdrawal(@Valid @RequestBody WithdrawalRequestDto dto) {
        log.trace("Withdrawing money from account with id: {}", dto.getAccountId());
        return transactionService.withdrawal(dto);
    }

    @PostMapping("/transfer")
    public TransactionResponseDto transfer(@Valid @RequestBody TransferRequestDto dto) {
        log.trace("Transferring money from account with id: {} to account with id: {}", dto.getFromAccountId(), dto.getToAccountId());
        return transactionService.transfer(dto);
    }

}
