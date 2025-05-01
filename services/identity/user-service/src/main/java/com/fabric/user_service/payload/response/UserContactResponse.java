package com.fabric.user_service.payload.response;

public record UserContactResponse(
        Long id,
        String type, // EMAIL, PHONE
        String value,
        boolean verified,
        boolean primary
) {}
