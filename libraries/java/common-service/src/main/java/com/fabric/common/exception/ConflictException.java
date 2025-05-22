package com.fabric.common.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends ApiException {

    public ConflictException(String message) {
        super("CONFLICT", message, HttpStatus.CONFLICT);
    }

    public ConflictException(String message, Throwable cause) {
        super("CONFLICT", message, HttpStatus.CONFLICT, cause);
    }
}
