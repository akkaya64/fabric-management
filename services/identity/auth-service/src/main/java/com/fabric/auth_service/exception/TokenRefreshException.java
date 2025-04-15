package com.fabric.auth_service.exception;

import java.io.Serial;

public class TokenRefreshException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String token;
    private final String message;

    public TokenRefreshException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
        this.token = token;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
