package com.zetta.rule.application.engine.transformation.expression;

import com.zetta.rule.application.util.JsonUtil;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.StringNode;

public class ConcatExpressionTransformation extends AbstractExpressionTransformation {
    public ConcatExpressionTransformation(JsonNode expression) {
        super(expression);
    }

    @Override
    public JsonNode apply(JsonNode data) {
        StringBuilder sb = new StringBuilder();
        for (JsonNode node : expression) {
            if (isJsonKey(node)) {
                JsonUtil.getFieldValue(data, node.asString()).ifPresent(value -> sb.append(value.asString()));
            } else {
                sb.append(node.asString());
            }
        }
        return new StringNode(sb.toString());
    }
}
