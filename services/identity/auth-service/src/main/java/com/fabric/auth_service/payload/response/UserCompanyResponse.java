package com.fabric.auth_service.payload.response;

record UserCompanyResponse(
        Long id,
        String name,
        String type, // INTERNAL, SUPPLIER, CUSTOMER, PARTNER
        String userRole
) {}
