package com.zetta.ruleengine.engine.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Condition {
    @JsonProperty("field")
    private String field;
    @JsonProperty("operator")
    private String operator;
    @JsonProperty("value")
    private Object value;
}
