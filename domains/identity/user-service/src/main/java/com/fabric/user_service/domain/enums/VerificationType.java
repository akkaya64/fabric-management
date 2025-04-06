package com.fabric.user_service.domain.enums;

public enum VerificationType {
    EMAIL("Email Verification"),
    PHONE("Phone Verification");

    private final String displayName;

    VerificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
