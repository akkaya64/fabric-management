package com.fabric.user_service.payload.response;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(
        Long id,
        String email,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<UserContactResponse> contacts,
        List<UserCompanyResponse> companies
) {}
