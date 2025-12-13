package com.zetta.ruleengine.engine.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ConditionDefinition {
    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("conditions")
    private List<Condition> conditions;
}
