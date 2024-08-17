package com.account.manager.demo.controller;

import com.account.manager.demo.dto.account.AccountInfoResponseDto;
import com.account.manager.demo.dto.account.CreateAccountRequestDto;
import com.account.manager.demo.service.AccountService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@OpenAPIDefinition(info = @Info(title = "Account Management API", version = "1.0", description = "Actions with accounts"))
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountControllerV1 {

    private final AccountService accountService;

    @Operation(
            operationId = "findAll",
            description = "Return list of all accounts",
            responses = {
                    @ApiResponse(
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = List.class)  // todo: array schema
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "System error", content = @Content())
            }
    )
    @GetMapping
    public List<AccountInfoResponseDto> findAll() {
        log.trace("Getting all accounts");
        return accountService.findAll();
    }

    @Operation(
            operationId = "getAccount",
            description = "Return account by id",
            responses = {
                    @ApiResponse(
                            content = @Content(
                                    schema = @Schema(implementation = AccountInfoResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "System error", content = @Content())
            }
    )
    @GetMapping("/{id}")
    public AccountInfoResponseDto getAccount(@PathVariable Long id) {
        log.trace("Getting account by id: {}", id);
        return accountService.findById(id);
    }

    @Operation(
            operationId = "createAccount",
            description = "Create new account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = CreateAccountRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            content = @Content(
                                    schema = @Schema(implementation = AccountInfoResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "System error", content = @Content())
            }
    )
    @PostMapping
    public AccountInfoResponseDto createAccount(@Valid @RequestBody CreateAccountRequestDto dto) {
        log.trace("Creating account: {}", dto);
        return accountService.create(dto);
    }

}
