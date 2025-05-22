package com.fabric.common.util.messages;

public class ErrorCodes {
    private ErrorCodes() {
        // Utility class
    }

    // Generic Error Codes
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String CONFLICT = "CONFLICT";
    public static final String UNSUPPORTED_MEDIA_TYPE = "UNSUPPORTED_MEDIA_TYPE";

    // User Error Codes
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String USER_EMAIL_EXISTS = "USER_EMAIL_EXISTS";
    public static final String USER_USERNAME_EXISTS = "USER_USERNAME_EXISTS";
    public static final String USER_INVALID_CREDENTIALS = "USER_INVALID_CREDENTIALS";
    public static final String USER_ACCOUNT_DISABLED = "USER_ACCOUNT_DISABLED";
    public static final String USER_ACCOUNT_LOCKED = "USER_ACCOUNT_LOCKED";
    public static final String USER_EMAIL_NOT_VERIFIED = "USER_EMAIL_NOT_VERIFIED";

    // Authentication Error Codes
    public static final String INVALID_TOKEN = "INVALID_TOKEN";
    public static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
    public static final String INSUFFICIENT_PERMISSIONS = "INSUFFICIENT_PERMISSIONS";
    public static final String INVALID_REFRESH_TOKEN = "INVALID_REFRESH_TOKEN";

    // File Error Codes
    public static final String FILE_NOT_FOUND = "FILE_NOT_FOUND";
    public static final String FILE_UPLOAD_FAILED = "FILE_UPLOAD_FAILED";
    public static final String FILE_SIZE_EXCEEDED = "FILE_SIZE_EXCEEDED";
    public static final String INVALID_FILE_TYPE = "INVALID_FILE_TYPE";

    // Business Logic Error Codes
    public static final String OPERATION_NOT_ALLOWED = "OPERATION_NOT_ALLOWED";
    public static final String INSUFFICIENT_BALANCE = "INSUFFICIENT_BALANCE";
    public static final String ORDER_ALREADY_PROCESSED = "ORDER_ALREADY_PROCESSED";
    public static final String INVALID_STATUS_TRANSITION = "INVALID_STATUS_TRANSITION";

    // Rate Limiting Codes
    public static final String RATE_LIMIT_EXCEEDED = "RATE_LIMIT_EXCEEDED";
    public static final String TOO_MANY_REQUESTS = "TOO_MANY_REQUESTS";

    // External Service Error Codes
    public static final String EXTERNAL_SERVICE_UNAVAILABLE = "EXTERNAL_SERVICE_UNAVAILABLE";
    public static final String PAYMENT_SERVICE_ERROR = "PAYMENT_SERVICE_ERROR";
    public static final String EMAIL_SERVICE_ERROR = "EMAIL_SERVICE_ERROR";
}
