package com.fabric.user_service.payload.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyContactRequest(
        @NotBlank(message = "Verification token is required")
        String token
) {}
