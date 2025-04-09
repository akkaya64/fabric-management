package com.fabric.user_service.domain.enums;

public enum UserStatus {
    PENDING("Onay Bekliyor"),
    ACTIVE("Aktif"),
    INACTIVE("Pasif"),
    LOCKED("Kilitli"),
    DELETED("Silinmiş");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
