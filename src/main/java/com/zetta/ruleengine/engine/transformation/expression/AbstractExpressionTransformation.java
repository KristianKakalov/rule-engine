package com.zetta.ruleengine.engine.transformation.expression;

import com.zetta.ruleengine.engine.transformation.Transformation;
import lombok.AllArgsConstructor;
import tools.jackson.databind.JsonNode;

@AllArgsConstructor
public abstract class AbstractExpressionTransformation implements Transformation {
    protected final JsonNode expression;

    protected boolean isJsonKey(JsonNode node) {
        return node.isString() && node.asString().contains(".") && !node.asString().trim().endsWith(".");
    }
}
