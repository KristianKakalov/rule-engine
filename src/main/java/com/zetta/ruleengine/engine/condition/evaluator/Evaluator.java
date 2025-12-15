package com.zetta.ruleengine.engine.condition.evaluator;

import tools.jackson.databind.JsonNode;

public interface Evaluator {
    boolean evaluate(JsonNode data);
}
