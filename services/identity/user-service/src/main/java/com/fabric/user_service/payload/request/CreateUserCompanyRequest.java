package com.fabric.user_service.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserCompanyRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotBlank(message = "Company name is required")
        String companyName,

        @NotBlank(message = "Company type is required")
        String companyType,

        @NotBlank(message = "User role is required")
        String userRole
) {}
