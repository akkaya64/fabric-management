package com.fabric.user_service.payload.response;

public record UserCompanyResponse(
        Long id,
        String name,
        String type, // INTERNAL, SUPPLIER, CUSTOMER, PARTNER
        String userRole
) {}
