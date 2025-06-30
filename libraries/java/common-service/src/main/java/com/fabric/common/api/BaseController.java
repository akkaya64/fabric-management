package com.fabric.common.api;

import com.fabric.common.util.messages.ErrorCodes;
import com.fabric.common.util.messages.ErrorMessages;
import com.fabric.common.util.messages.SuccessMessages;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * Provides common response methods for the controller layer.
 * Messages are centrally managed through ErrorMessages and SuccessMessages classes.
 */
public abstract class BaseController {

    // Success responses
    protected <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.success(data, SuccessMessages.DATA_RETRIEVED));
    }

    protected <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return ResponseEntity.ok(ApiResponse.success(data, message));
    }

    protected <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(data, SuccessMessages.RESOURCE_CREATED));
    }

    protected <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(data, message));
    }

    protected ResponseEntity<ApiResponse<Void>> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(SuccessMessages.OPERATION_SUCCESSFUL));
    }

    protected ResponseEntity<ApiResponse<Void>> noContent(String message) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(message));
    }

    // Paged responses
    protected <T> ResponseEntity<PagedResponse<T>> ok(Page<T> page) {
        PagedResponse.PageInfo pageInfo = PagedResponse.PageInfo.from(page);
        return ResponseEntity.ok(PagedResponse.of(page.getContent(), pageInfo, SuccessMessages.DATA_RETRIEVED));
    }

    protected <T> ResponseEntity<PagedResponse<T>> ok(Page<T> page, String message) {
        PagedResponse.PageInfo pageInfo = PagedResponse.PageInfo.from(page);
        return ResponseEntity.ok(PagedResponse.of(page.getContent(), pageInfo, message));
    }

    // Error responses with centralized messages
    protected <T> ResponseEntity<ApiResponse<T>> error(String code, String message, int status) {
        ApiError error = ApiError.of(code, message, generateRequestId());
        return ResponseEntity.status(status)
                .body(ApiResponse.error(error));
    }

    protected ResponseEntity<ApiResponse<Void>> badRequest(String code, String message) {
        return error(code, message, HttpStatus.BAD_REQUEST.value());
    }

    protected ResponseEntity<ApiResponse<Void>> badRequest() {
        return error(ErrorCodes.BAD_REQUEST, ErrorMessages.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value());
    }

    protected ResponseEntity<ApiResponse<Void>> unauthorized() {
        return error(ErrorCodes.UNAUTHORIZED, ErrorMessages.UNAUTHORIZED_ACCESS,
                HttpStatus.UNAUTHORIZED.value());
    }

    protected ResponseEntity<ApiResponse<Void>> unauthorized(String message) {
        return error(ErrorCodes.UNAUTHORIZED, message, HttpStatus.UNAUTHORIZED.value());
    }

    protected ResponseEntity<ApiResponse<Void>> forbidden() {
        return error(ErrorCodes.FORBIDDEN, ErrorMessages.FORBIDDEN_ACCESS,
                HttpStatus.FORBIDDEN.value());
    }

    protected ResponseEntity<ApiResponse<Void>> forbidden(String message) {
        return error(ErrorCodes.FORBIDDEN, message, HttpStatus.FORBIDDEN.value());
    }

    protected ResponseEntity<ApiResponse<Void>> notFound() {
        return error(ErrorCodes.NOT_FOUND, ErrorMessages.RESOURCE_NOT_FOUND,
                HttpStatus.NOT_FOUND.value());
    }

    protected ResponseEntity<ApiResponse<Void>> notFound(String code, String message) {
        return error(code, message, HttpStatus.NOT_FOUND.value());
    }

    protected ResponseEntity<ApiResponse<Void>> conflict(String code, String message) {
        return error(code, message, HttpStatus.CONFLICT.value());
    }

    protected ResponseEntity<ApiResponse<Void>> internalServerError() {
        return error(ErrorCodes.INTERNAL_SERVER_ERROR, ErrorMessages.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    protected ResponseEntity<ApiResponse<Void>> internalServerError(String code, String message) {
        return error(code, message, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    // Validation error response
    protected ResponseEntity<ApiResponse<Void>> validationError(List<ValidationError> validationErrors) {
        ApiError error = ApiError.withValidationErrors(
                ErrorCodes.VALIDATION_ERROR,
                ErrorMessages.VALIDATION_FAILED,
                validationErrors
        ).toBuilder()
                .requestId(generateRequestId())
                .build();
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(error));
    }

    // Utility method
    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}
