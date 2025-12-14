package com.zetta.ruleengine.engine.condition.creator;

import com.zetta.ruleengine.engine.condition.*;
import com.zetta.ruleengine.engine.condition.dto.ConditionDto;
import com.zetta.ruleengine.engine.condition.dto.ConditionExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class ConditionFactory {
    private static final Logger log = LoggerFactory.getLogger(ConditionFactory.class);

    private ConditionFactory() {

    }

    public static Condition create(ConditionDto conditionDto) {
        String id = conditionDto.getId();
        List<ConditionExpression> conditionExpressions = conditionDto.getConditionExpressions();

        if (conditionExpressions == null) {
            log.debug("Condition expressions are null for condition id: {}", id);
            conditionExpressions = new LinkedList<>();
        }

        return switch (LogicalOperation.valueOf(conditionDto.getLogicalOperation().toUpperCase())) {
            case ALL -> new AllConditions(id, conditionExpressions);
            case ANY -> new AnyConditions(id, conditionExpressions);
            case NONE -> new NoneConditions(id, conditionExpressions);
        };
    }
}
