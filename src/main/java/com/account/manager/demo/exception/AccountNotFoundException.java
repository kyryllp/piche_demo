package com.account.manager.demo.exception;

import com.account.manager.demo.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends ApplicationBaseException {

    private static final String ERROR_MESSAGE = "Account with id=%d not found";

    public AccountNotFoundException(Long accountId) {
        super(ERROR_MESSAGE.formatted(accountId), HttpStatus.NOT_FOUND, ErrorCode.ACCOUNT_NOT_FOUND);
    }
}
