package com.zetta.rule.application.engine.transformation.expression;

import com.zetta.rule.application.util.JsonUtil;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.DoubleNode;
import tools.jackson.databind.node.NullNode;

public class AddExpressionTransformation extends AbstractExpressionTransformation {

    public AddExpressionTransformation(JsonNode expression) {
        super(expression);
    }

    @Override
    public JsonNode apply(JsonNode data) {
        double sum = 0;
        for (JsonNode node : expression) {
            if (isJsonKey(node)) {
                sum += JsonUtil.getFieldValue(data, node.asString()).orElse(NullNode.getInstance()).asDouble();
            } else {
                sum += node.asDouble();
            }
        }
        return new DoubleNode(sum);
    }
}
