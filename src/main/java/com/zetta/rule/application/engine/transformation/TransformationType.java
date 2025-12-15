package com.zetta.rule.application.engine.transformation;

public enum TransformationType {
    SET("set"),
    INCREMENT("increment"),
    REMOVE("remove");

    private final String value;

    TransformationType(String value) {
        this.value = value;
    }

    public static boolean isValid(String value) {
        if (value == null) return false;

        for (TransformationType transformationType : TransformationType.values()) {
            if (transformationType.value.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
