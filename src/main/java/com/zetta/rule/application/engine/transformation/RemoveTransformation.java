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
public class RemoveTransformation implements Transformation {
    private final String target;

    @Override
    public JsonNode apply(JsonNode data) {
        return JsonUtil.removeField(data, target);
    }
}
