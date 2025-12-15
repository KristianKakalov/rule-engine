package com.zetta.rule.application.engine.condition;


import com.zetta.rule.application.engine.condition.dto.ConditionExpression;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
public abstract class AbstractCondition implements Condition {
    @Getter
    protected final String id;
    protected final List<ConditionExpression> conditionExpressions;
}
