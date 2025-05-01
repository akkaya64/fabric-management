package com.fabric.auth_service.payload.response;

public record CompanyInfo(
        Long id,
        String name,
        String type,
        String role
) {}
