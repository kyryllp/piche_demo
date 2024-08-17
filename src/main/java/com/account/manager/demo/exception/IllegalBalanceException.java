package com.account.manager.demo.exception;

import com.account.manager.demo.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class IllegalBalanceException extends ApplicationBaseException {

    public IllegalBalanceException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.ILLEGAL_BALANCE);
    }
}
