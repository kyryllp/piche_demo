package com.account.manager.demo.exception;

import com.account.manager.demo.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApplicationBaseException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final ErrorCode errorCode;

    public ApplicationBaseException(String message, HttpStatus httpStatus, ErrorCode errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
