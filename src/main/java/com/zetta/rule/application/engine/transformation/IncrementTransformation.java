package com.zetta.rule.application.engine.transformation;

import com.zetta.rule.application.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import tools.jackson.databind.JsonNode;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class IncrementTransformation implements Transformation {
    private final String target;
    private final JsonNode value;

    @Override
    public JsonNode apply(JsonNode data) {
        return value.isNumber() ? JsonUtil.incrementField(data, target, value.asInt()) : data;
    }
}
