package com.zetta.ruleengine.engine.condition;

import com.zetta.ruleengine.engine.condition.dto.ConditionExpression;
import com.zetta.ruleengine.engine.condition.evaluator.ConditionExpressionEvaluatorUtil;
import tools.jackson.databind.JsonNode;

import java.util.List;

public class AnyConditions extends AbstractCondition{
    public AnyConditions(String id, List<ConditionExpression> conditions) {
        super(id, conditions);
    }

    @Override
    public boolean evaluate(JsonNode data) {
        return conditionExpressions.stream()
                .anyMatch(conditionExpression -> ConditionExpressionEvaluatorUtil.evaluateCondition(conditionExpression, data));
    }
}
