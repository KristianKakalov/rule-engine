package com.zetta.ruleengine.engine.condition;

import lombok.Getter;

@Getter
public enum LogicalOperation {
    ALL("all"),
    ANY("any"),
    NONE("none");

    private final String value;

    LogicalOperation(String value) {
        this.value = value;
    }

    public static boolean isValid(String value) {
        if (value == null) return false;

        for (LogicalOperation operation : LogicalOperation.values()) {
            if (operation.value.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}