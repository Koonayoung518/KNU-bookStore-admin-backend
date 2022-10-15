package com.knubookStore.knubookStoreadmin.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_001", " AUTHENTICATION_FAILED."),
    AUTHENTICATION_CONFLICT(HttpStatus.CONFLICT, "AUTH_002","AUTHENTICATION_CONFLICT."),
    NOT_FOUND_ADMIN(HttpStatus.NOT_FOUND, "AUTH_003","NOT_FOUND_ADMIN"),
    NOT_FOUND_BOOK(HttpStatus.NOT_FOUND, "BOOK_001","NOT_FOUND_BOOK"),
    BOOK_DUPLICATED(HttpStatus.FORBIDDEN, "BOOK_002","BOOK_DUPLICATED"),
    SELL_BOOK_FAILED(HttpStatus.BAD_REQUEST, "BOOK_003", "SELL_BOOK_FAILED"),
    NOT_FOUND_HISTORY(HttpStatus.NOT_FOUND, "HISTORY_001","NOT_FOUND_HISTORY");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message){
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
