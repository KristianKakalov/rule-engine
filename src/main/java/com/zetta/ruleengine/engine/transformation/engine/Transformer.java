package com.zetta.ruleengine.engine.transformation.engine;

import tools.jackson.databind.JsonNode;

public interface Transformer {
    JsonNode transform(JsonNode data);
}
