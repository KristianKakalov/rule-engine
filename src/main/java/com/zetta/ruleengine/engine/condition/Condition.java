package com.zetta.ruleengine.engine.condition;

import tools.jackson.databind.JsonNode;

public interface Condition {
    String getId();

    boolean evaluate(JsonNode data);
}
