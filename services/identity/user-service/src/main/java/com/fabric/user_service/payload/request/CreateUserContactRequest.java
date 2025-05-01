package com.fabric.user_service.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserContactRequest(
        @NotBlank(message = "Contact type is required")
        String type, // EMAIL, PHONE

        @NotBlank(message = "Contact value is required")
        String value,

        @NotNull(message = "Primary flag is required")
        boolean primary
) {}
