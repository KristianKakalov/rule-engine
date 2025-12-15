package com.zetta.ruleengine.engine.transformation;

import com.zetta.ruleengine.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import tools.jackson.databind.JsonNode;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class SetTransformation implements Transformation {
    private final String target;
    private final Transformation expressionTransformation;

    @Override
    public JsonNode apply(JsonNode data) {
        return JsonUtil.updateField(data, target, expressionTransformation.apply(data));
    }
}
