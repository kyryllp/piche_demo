package com.account.manager.demo.config;

import com.account.manager.demo.dto.ErrorInfoDto;
import com.account.manager.demo.enums.ErrorCode;
import com.account.manager.demo.exception.ApplicationBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
class ExceptionHandlerConfig {

    @ExceptionHandler({ApplicationBaseException.class})
    public ResponseEntity<ErrorInfoDto> handleApplicationExceptions(Exception exception, WebRequest request) {
        ApplicationBaseException applicationException = ((ApplicationBaseException) exception);

        return ResponseEntity
                .status(applicationException.getHttpStatus())
                .body(ErrorInfoDto.builder()
                        .message(applicationException.getMessage())
                        .errorCode(applicationException.getErrorCode())
                        .build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorInfoDto> handleValidationException(MethodArgumentNotValidException exception, WebRequest request) {
        StringBuilder strBuilder = new StringBuilder();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName;
            try {
                fieldName = ((FieldError) error).getField();

            } catch (ClassCastException ex) {
                fieldName = error.getObjectName();
            }
            String message = error.getDefaultMessage();
            strBuilder.append(String.format("%s: %s\n", fieldName, message));
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorInfoDto.builder()
                        .message(strBuilder.toString())
                        .errorCode(ErrorCode.BAD_REQUEST)
                        .build());
    }

}
