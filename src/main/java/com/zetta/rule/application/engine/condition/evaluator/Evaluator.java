package com.zetta.rule.application.engine.condition.evaluator;

import tools.jackson.databind.JsonNode;

public interface Evaluator {
    boolean evaluate(JsonNode data);
}
