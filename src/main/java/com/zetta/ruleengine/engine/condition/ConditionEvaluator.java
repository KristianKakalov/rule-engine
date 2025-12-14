package com.zetta.ruleengine.engine.condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Optional;

@Component
public class ConditionEvaluator {
    private static final Logger log = LoggerFactory.getLogger(ConditionEvaluator.class);

    private final List<ConditionGroup> conditionGroups;

    public ConditionEvaluator(List<ConditionGroup> conditionGroups) {
        this.conditionGroups = conditionGroups;
    }

    public boolean evaluate(JsonNode data) {
        for (ConditionGroup conditionGroup : conditionGroups) {
            if (evaluateConditionGroup(conditionGroup, data)) {
                log.info("Matched {} conditions for {}", conditionGroup.id(), data);
                return true;
            }
        }
        log.info("No matched conditions for {}", data);
        return false;
    }

    public boolean evaluateConditionGroup(ConditionGroup conditionGroup, JsonNode data) {
        String type = conditionGroup.logicalOperation();
        List<Condition> conditions = conditionGroup.conditions();

        if (conditions == null || conditions.isEmpty()) {
            log.debug("No conditions to evaluate for {}", conditionGroup.id());
            return true;
        }

        log.debug("Evaluating condition group {} (logicalOperation: {})", conditionGroup.id(), type);

        return switch (type.toLowerCase()) {
            case "all" -> evaluateAll(conditions, data);
            case "any" -> evaluateAny(conditions, data);
            case "none" -> evaluateNone(conditions, data);
            default -> {
                log.warn("Unknown condition logicalOperation: {}", type);
                yield false;
            }
        };
    }

    private boolean evaluateAll(List<Condition> conditions, JsonNode data) {
        return conditions.stream()
                .allMatch(condition -> evaluateCondition(condition, data));
    }

    private boolean evaluateAny(List<Condition> conditions, JsonNode data) {
        return conditions.stream()
                .anyMatch(condition -> evaluateCondition(condition, data));
    }

    private boolean evaluateNone(List<Condition> conditions, JsonNode data) {
        return conditions.stream()
                .noneMatch(condition -> evaluateCondition(condition, data));
    }

    private boolean evaluateCondition(Condition condition, JsonNode data) {
        try {
            String fieldPath = condition.field();
            JsonNode expectedValue = condition.value();
            Optional<JsonNode> fieldValueOptional = getFieldValue(data, fieldPath);

            if (fieldValueOptional.isEmpty()) {
                log.debug("Field {} not found or null", fieldPath);
                return false;
            }

            return compareValues(fieldValueOptional.get(), condition.operator(), expectedValue);

        } catch (Exception e) {
            log.error("Error evaluating condition {}: {}", condition, e.getMessage());
            return false;
        }
    }

    private Optional<JsonNode> getFieldValue(JsonNode data, String fieldPath) {
        String[] parts = fieldPath.split("\\.");
        JsonNode currentNode = data;

        for (String part : parts) {
            if (currentNode == null || currentNode.isNull()) {
                return Optional.empty();
            }
            currentNode = currentNode.get(part);
        }

        return Optional.of(currentNode);
    }

    private boolean compareValues(JsonNode actualValue, String operator, JsonNode expectedValue) {
        try {
            return switch (operator) {
                case "==" -> compareEquals(actualValue, expectedValue);
                case "!=" -> !compareEquals(actualValue, expectedValue);
                case ">" -> compareGreaterThan(actualValue, expectedValue);
                case ">=" -> compareGreaterThanOrEqual(actualValue, expectedValue);
                case "<" -> compareLessThan(actualValue, expectedValue);
                case "<=" -> compareLessThanOrEqual(actualValue, expectedValue);
                default -> {
                    log.warn("Unknown operator: {}", operator);
                    yield false;
                }
            };
        } catch (Exception e) {
            log.error("Comparison error: {} {} {}", actualValue, operator, expectedValue, e);
            return false;
        }
    }

    private boolean compareEquals(JsonNode actualValue, JsonNode expectedValue) {
        if (actualValue.isNumber() && expectedValue.isNumber()) {
            return actualValue.asDouble() == expectedValue.asDouble();
        } else if (actualValue.isBoolean() && expectedValue.isBoolean()) {
            return actualValue.asBoolean() == expectedValue.asBoolean();
        } else {
            return actualValue.asString().equals(expectedValue.asString());
        }
    }

    private boolean compareGreaterThan(JsonNode actualValue, JsonNode expectedValue) {
        if (actualValue.isNumber() && expectedValue.isNumber()) {
            return actualValue.asDouble() > expectedValue.asDouble();
        }
        return false;
    }

    private boolean compareGreaterThanOrEqual(JsonNode actualValue, JsonNode expectedValue) {
        if (actualValue.isNumber() && expectedValue.isNumber()) {
            return actualValue.asDouble() >= expectedValue.asDouble();
        }
        return false;
    }

    private boolean compareLessThan(JsonNode actualValue, JsonNode expectedValue) {
        if (actualValue.isNumber() && expectedValue.isNumber()) {
            return actualValue.asDouble() < expectedValue.asDouble();
        }
        return false;
    }

    private boolean compareLessThanOrEqual(JsonNode actualValue, JsonNode expectedValue) {
        if (actualValue.isNumber() && expectedValue.isNumber()) {
            return actualValue.asDouble() <= expectedValue.asDouble();
        }
        return false;
    }
}
