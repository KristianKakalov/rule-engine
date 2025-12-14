package com.zetta.ruleengine.engine.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ConditionGroup(@JsonProperty("id") String id,
                             @JsonProperty("type") String logicalOperation,
                             @JsonProperty("conditions") List<Condition> conditions) {
}
