package com.zetta.rule.application.util;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.Optional;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode removeField(JsonNode json, String fieldPath) {
        ObjectNode mutable = (ObjectNode) json.deepCopy();
        String[] parts = fieldPath.split("\\.");

        if (parts.length == 1) {
            mutable.remove(fieldPath);
            return mutable;
        }

        ObjectNode current = mutable;
        for (int i = 0; i < parts.length - 1; i++) {
            JsonNode node = current.get(parts[i]);
            if (node == null || !node.isObject()) {
                return mutable;
            }
            current = (ObjectNode) node;
        }
        current.remove(parts[parts.length - 1]);

        return mutable;
    }

    public static JsonNode updateField(JsonNode json, String fieldPath, Object value) {
        ObjectNode mutable = (ObjectNode) json.deepCopy();
        String[] parts = fieldPath.split("\\.");

        ObjectNode current = mutable;
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            JsonNode next = current.get(part);
            if (next == null || !next.isObject()) {
                current.set(part, mapper.createObjectNode());
            }
            current = (ObjectNode) current.get(part);
        }

        current.set(parts[parts.length - 1], mapper.valueToTree(value));
        return mutable;
    }

    public static JsonNode incrementField(JsonNode json, String fieldPath, int incrementBy) {
        Optional<JsonNode> currentValue = getFieldValue(json, fieldPath);
        if (currentValue.isPresent() && currentValue.get().isNumber()) {
            int newValue = currentValue.get().asInt() + incrementBy;
            return updateField(json, fieldPath, newValue);
        }
        return json;
    }

    public static Optional<JsonNode> getFieldValue(JsonNode data, String fieldPath) {
        if (data.has(fieldPath)) {
            return Optional.ofNullable(data.get(fieldPath));
        }

        String[] parts = fieldPath.split("\\.");
        JsonNode currentNode = data;

        for (String part : parts) {
            if (currentNode == null || currentNode.isNull()) {
                return Optional.empty();
            }
            currentNode = currentNode.get(part);
        }

        return Optional.ofNullable(currentNode);
    }
}
