package com.fabric.auth_service.payload.response;

record UserContactResponse(
        Long id,
        String type, // EMAIL, PHONE
        String value,
        boolean verified,
        boolean primary
) {}
