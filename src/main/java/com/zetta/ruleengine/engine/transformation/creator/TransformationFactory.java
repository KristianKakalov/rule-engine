package com.zetta.ruleengine.engine.transformation.creator;

import com.zetta.ruleengine.engine.transformation.*;
import com.zetta.ruleengine.engine.transformation.dto.TransformationRule;
import com.zetta.ruleengine.engine.transformation.expression.AddExpressionTransformation;
import com.zetta.ruleengine.engine.transformation.expression.ConcatExpressionTransformation;

public class TransformationFactory {

    public static final String ADD_EXPRESSION = "add";
    public static final String CONCAT_EXPRESSION = "concat";

    private TransformationFactory() {
    }

    public static Transformation create(TransformationRule rule) {
        switch (TransformationType.valueOf(rule.getType().toUpperCase())) {
            case INCREMENT:
                return new IncrementTransformation(rule.getTarget(), rule.getValue());
            case REMOVE:
                return new RemoveTransformation(rule.getTarget());
            case SET: {
                if (rule.getValue().has(ADD_EXPRESSION)) {
                    return new SetTransformation(rule.getTarget(), new AddExpressionTransformation(rule.getValue().get(ADD_EXPRESSION)));
                }
                if (rule.getValue().has(CONCAT_EXPRESSION)) {
                    return new SetTransformation(rule.getTarget(), new ConcatExpressionTransformation(rule.getValue().get(CONCAT_EXPRESSION)));
                } else {
                    throw new UnsupportedOperationException("Unsupported operation " + rule.getValue().asString() + " for rule: " + rule);
                }
            }
            default:
                throw new UnsupportedOperationException("Unsupported operation " + rule.getValue().asString() + " for rule: " + rule);
        }
    }
}
