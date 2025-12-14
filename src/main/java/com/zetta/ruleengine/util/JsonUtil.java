package com.zetta.ruleengine.util;

import tools.jackson.databind.JsonNode;

import java.util.Optional;

public class JsonUtil {
    public static Optional<JsonNode> getFieldValue(JsonNode data, String fieldPath) {
        String[] parts = fieldPath.split("\\.");
        JsonNode currentNode = data;

        for (String part : parts) {
            if (currentNode == null || currentNode.isNull()) {
                return Optional.empty();
            }
            currentNode = currentNode.get(part);
        }

        return Optional.of(currentNode);
    }
}
