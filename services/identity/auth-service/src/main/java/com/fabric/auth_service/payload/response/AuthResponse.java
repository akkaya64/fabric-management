package com.fabric.auth_service.payload.response;

import java.util.List;

public record AuthResponse(
        String accessToken,
        Long userId,
        String email,
        Long companyId,
        String companyName,
        String companyType,
        List<String> roles,
        List<CompanyInfo> availableCompanies
) {}
