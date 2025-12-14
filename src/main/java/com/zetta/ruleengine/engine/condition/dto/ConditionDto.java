package com.zetta.ruleengine.engine.condition.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ConditionDto {
    private final String id;
    private final String logicalOperation;
    private final List<ConditionExpression> conditionExpressions;
}
