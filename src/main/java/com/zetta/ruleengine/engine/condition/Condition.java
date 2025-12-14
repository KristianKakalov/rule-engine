package com.zetta.ruleengine.engine.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.JsonNode;

public record Condition(@JsonProperty("field") String field,
                        @JsonProperty("operator") String operator,
                        @JsonProperty("value") JsonNode value) {}