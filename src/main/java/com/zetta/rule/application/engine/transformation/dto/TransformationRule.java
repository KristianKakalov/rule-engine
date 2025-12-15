package com.zetta.rule.application.engine.transformation.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import tools.jackson.databind.JsonNode;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TransformationRule {
    private final String type;
    private final String target;
    private final JsonNode value;
}
