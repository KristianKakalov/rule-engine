package com.zetta.rule.application.engine.condition;

import com.zetta.rule.application.engine.condition.dto.ConditionExpression;
import com.zetta.rule.application.engine.condition.evaluator.util.ConditionExpressionEvaluatorUtil;
import tools.jackson.databind.JsonNode;

import java.util.List;

public class NoneConditions extends AbstractCondition{
    public NoneConditions(String id, List<ConditionExpression> conditions) {
        super(id, conditions);
    }

    @Override
    public boolean evaluate(JsonNode data) {
        return conditionExpressions.stream()
                .noneMatch(conditionExpression -> ConditionExpressionEvaluatorUtil.evaluateCondition(conditionExpression, data));
    }
}
