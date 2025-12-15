package com.zetta.rule.application.engine.transformation.engine;

import tools.jackson.databind.JsonNode;

public interface Transformer {
    JsonNode transform(JsonNode data);
}
