package com.fabric.common.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private String code;
    private String message;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Instant timestamp;
    
    private List<ValidationError> validationErrors;
    private String requestId;
    private String path;
    
    public static ApiError of(String code, String message) {
        return ApiError.builder()
                .code(code)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }
    
    public static ApiError of(String code, String message, String requestId) {
        return ApiError.builder()
                .code(code)
                .message(message)
                .timestamp(Instant.now())
                .requestId(requestId)
                .build();
    }
    
    public static ApiError withValidationErrors(String code, String message, List<ValidationError> validationErrors) {
        return ApiError.builder()
                .code(code)
                .message(message)
                .timestamp(Instant.now())
                .validationErrors(validationErrors)
                .build();
    }
}
