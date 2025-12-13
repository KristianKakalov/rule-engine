package com.zetta.ruleengine.engine.transformation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransformationRule {
    @JsonProperty("type")
    private String type;
    @JsonProperty("target")
    private String target;
    @JsonProperty("expression")
    private String expression;
    @JsonProperty("value")
    private Integer value;
}
