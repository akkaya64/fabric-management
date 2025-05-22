package com.fabric.common.util.messages;

public class ErrorMessages {
    private ErrorMessages() {
        // Utility class - instantiation'a izin verme
    }

    // Generic Error Messages
    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred. Please try again later.";
    public static final String VALIDATION_FAILED = "Input validation failed";
    public static final String CONSTRAINT_VALIDATION_FAILED = "Constraint validation failed";
    public static final String UNAUTHORIZED_ACCESS = "You are not authorized to access this resource";
    public static final String FORBIDDEN_ACCESS = "Access to this resource is forbidden";
    public static final String RESOURCE_NOT_FOUND = "Requested resource not found";
    public static final String BAD_REQUEST = "Invalid request parameters";

    // User Related Error Messages
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_EMAIL_EXISTS = "A user with this email already exists";
    public static final String USER_USERNAME_EXISTS = "This username is already taken";
    public static final String USER_INVALID_CREDENTIALS = "Invalid email or password";
    public static final String USER_ACCOUNT_DISABLED = "User account has been disabled";
    public static final String USER_ACCOUNT_LOCKED = "User account has been locked";
    public static final String USER_EMAIL_NOT_VERIFIED = "Email address has not been verified";

    // Authentication & Authorization Error Messages
    public static final String INVALID_TOKEN = "Invalid or expired authentication token";
    public static final String TOKEN_EXPIRED = "Authentication token has expired";
    public static final String INSUFFICIENT_PERMISSIONS = "You don't have sufficient permissions for this operation";
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token";

    // File Related Error Messages
    public static final String FILE_NOT_FOUND = "File not found";
    public static final String FILE_UPLOAD_FAILED = "File upload failed";
    public static final String FILE_SIZE_EXCEEDED = "File size exceeds maximum allowed limit";
    public static final String INVALID_FILE_TYPE = "Invalid file type";
    public static final String FILE_PROCESSING_FAILED = "File processing failed";

    // Database Related Error Messages
    public static final String DATABASE_CONNECTION_ERROR = "Database connection error";
    public static final String DATA_INTEGRITY_VIOLATION = "Data integrity constraint violated";
    public static final String DUPLICATE_ENTRY = "Duplicate entry found";
    public static final String FOREIGN_KEY_CONSTRAINT = "Cannot delete record due to existing dependencies";

    // Business Logic Error Messages
    public static final String OPERATION_NOT_ALLOWED = "This operation is not allowed";
    public static final String INSUFFICIENT_BALANCE = "Insufficient balance for this transaction";
    public static final String ORDER_ALREADY_PROCESSED = "Order has already been processed";
    public static final String INVALID_STATUS_TRANSITION = "Invalid status transition";

    // Validation Specific Messages
    public static final String REQUIRED_FIELD_MISSING = "Required field is missing";
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String INVALID_PHONE_FORMAT = "Invalid phone number format";
    public static final String PASSWORD_TOO_WEAK = "Password does not meet security requirements";
    public static final String INVALID_DATE_FORMAT = "Invalid date format";
    public static final String INVALID_DATE_RANGE = "Invalid date range";

    // Rate Limiting Messages
    public static final String RATE_LIMIT_EXCEEDED = "Rate limit exceeded. Please try again later";
    public static final String TOO_MANY_REQUESTS = "Too many requests. Please slow down";

    // External Service Error Messages
    public static final String EXTERNAL_SERVICE_UNAVAILABLE = "External service is currently unavailable";
    public static final String PAYMENT_SERVICE_ERROR = "Payment processing failed";
    public static final String EMAIL_SERVICE_ERROR = "Email service is temporarily unavailable";
    public static final String SMS_SERVICE_ERROR = "SMS service is temporarily unavailable";
}
