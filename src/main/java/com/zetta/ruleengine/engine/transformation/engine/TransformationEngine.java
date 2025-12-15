package com.zetta.ruleengine.engine.transformation.engine;

import com.zetta.ruleengine.engine.transformation.Transformation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.List;

@Component
@AllArgsConstructor
public class TransformationEngine implements Transformer {

    private final List<Transformation> transformations;

    @Override
    public JsonNode transform(JsonNode data) {
        for (Transformation transformation : transformations) {
            data = transformation.apply(data);
        }
        return data;
    }
}
