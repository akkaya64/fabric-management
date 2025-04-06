package com.fabric.user_service.domain.enums;

public enum CompetencyLevel {
    ENTRY("Entry Level"),
    JUNIOR("Junior"),
    INTERMEDIATE("Intermediate"),
    SENIOR("Senior"),
    EXPERT("Expert"),
    MASTER("Master");

    private final String displayName;

    CompetencyLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
