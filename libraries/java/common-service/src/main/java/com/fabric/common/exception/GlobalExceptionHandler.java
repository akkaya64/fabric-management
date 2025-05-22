package com.fabric.common.exception;

import com.fabric.common.api.ApiError;
import com.fabric.common.api.ApiResponse;
import com.fabric.common.api.ValidationError;
import com.fabric.common.util.messages.ErrorCodes;
import com.fabric.common.util.messages.ErrorMessages;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Global exception handler - tüm uygulamadaki hataları yakalar ve standart formatta döner.
 * Mesajlar merkezi olarak ErrorMessages sınıfından alınır.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        String requestId = generateRequestId();
        log.warn("Validation error occurred. RequestId: {}", requestId, ex);

        List<ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .code(ErrorCodes.VALIDATION_ERROR)
                .message(ErrorMessages.VALIDATION_FAILED)
                .timestamp(Instant.now())
                .validationErrors(validationErrors)
                .requestId(requestId)
                .build();

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(apiError));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {

        String requestId = generateRequestId();
        log.warn("Constraint violation occurred. RequestId: {}", requestId, ex);

        List<ValidationError> validationErrors = ex.getConstraintViolations()
                .stream()
                .map(this::mapConstraintViolation)
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .code(ErrorCodes.VALIDATION_ERROR)
                .message(ErrorMessages.CONSTRAINT_VALIDATION_FAILED)
                .timestamp(Instant.now())
                .validationErrors(validationErrors)
                .requestId(requestId)
                .build();

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(apiError));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException ex, WebRequest request) {

        String requestId = generateRequestId();
        log.warn("Business exception occurred. RequestId: {}, Code: {}", requestId, ex.getCode(), ex);

        ApiError apiError = ApiError.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(ex.getStatus())
                .body(ApiResponse.error(apiError));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        String requestId = generateRequestId();
        log.warn("Resource not found. RequestId: {}", requestId, ex);

        ApiError apiError = ApiError.builder()
                .code(ErrorCodes.NOT_FOUND)
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(apiError));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex, WebRequest request) {

        String requestId = generateRequestId();
        log.error("Unexpected error occurred. RequestId: {}", requestId, ex);

        ApiError apiError = ApiError.builder()
                .code(ErrorCodes.INTERNAL_SERVER_ERROR)
                .message(ErrorMessages.INTERNAL_SERVER_ERROR)
                .timestamp(Instant.now())
                .requestId(requestId)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(apiError));
    }

    private ValidationError mapFieldError(FieldError fieldError) {
        return ValidationError.of(
                fieldError.getField(),
                fieldError.getDefaultMessage(),
                fieldError.getRejectedValue()
        );
    }

    private ValidationError mapConstraintViolation(ConstraintViolation<?> violation) {
        return ValidationError.of(
                violation.getPropertyPath().toString(),
                violation.getMessage(),
                violation.getInvalidValue()
        );
    }

    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}