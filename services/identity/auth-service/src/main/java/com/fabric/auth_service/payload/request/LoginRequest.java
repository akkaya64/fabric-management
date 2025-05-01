package com.fabric.auth_service.payload.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Login identifier (email or phone) is required")
        String identifier,

        @NotBlank(message = "Password is required")
        String password
) {}
