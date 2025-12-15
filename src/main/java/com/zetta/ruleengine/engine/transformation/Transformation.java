package com.zetta.ruleengine.engine.transformation;

import tools.jackson.databind.JsonNode;

public interface Transformation {
    JsonNode apply(JsonNode data);
}
