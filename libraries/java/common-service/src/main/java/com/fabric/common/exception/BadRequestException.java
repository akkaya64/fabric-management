package com.fabric.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super("BAD_REQUEST", message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message, Throwable cause) {
        super("BAD_REQUEST", message, HttpStatus.BAD_REQUEST, cause);
    }
}
