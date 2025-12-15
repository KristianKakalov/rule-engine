package com.zetta.rule.application.engine.transformation;

import tools.jackson.databind.JsonNode;

public interface Transformation {
    JsonNode apply(JsonNode data);
}
