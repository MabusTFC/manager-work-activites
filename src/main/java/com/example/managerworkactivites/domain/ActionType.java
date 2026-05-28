package com.example.managerworkactivites.domain;

public enum ActionType {
    WORK("Работа"),
    BREAK("Перерыв");

    private final String displayName;

    ActionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
