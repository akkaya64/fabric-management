package com.fabric.auth_service.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CompanySelectionRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "User ID is required")
        Long userId,

        @NotBlank(message = "Access token is required")
        String accessToken
) {}
