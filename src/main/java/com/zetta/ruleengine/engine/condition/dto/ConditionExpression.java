package com.zetta.ruleengine.engine.condition.dto;

import lombok.*;
import tools.jackson.databind.JsonNode;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ConditionExpression {
    private final String field;
    private final String operator;
    private final JsonNode value;
}