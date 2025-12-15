package com.zetta.rule.application.engine.condition;

import tools.jackson.databind.JsonNode;

public interface Condition {
    String getId();

    boolean evaluate(JsonNode data);
}
