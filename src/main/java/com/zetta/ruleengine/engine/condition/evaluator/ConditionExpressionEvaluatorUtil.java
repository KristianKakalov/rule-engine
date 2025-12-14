package com.zetta.ruleengine.engine.condition.evaluator;

import com.zetta.ruleengine.engine.condition.dto.ConditionExpression;
import com.zetta.ruleengine.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.JsonNode;

import java.util.Optional;

public class ConditionExpressionEvaluatorUtil {
    private static final Logger log = LoggerFactory.getLogger(ConditionExpressionEvaluatorUtil.class);

    public static boolean evaluateCondition(ConditionExpression conditionExpression, JsonNode data) {
        try {
            String fieldPath = conditionExpression.getField();
            JsonNode expectedValue = conditionExpression.getValue();
            Optional<JsonNode> fieldValueOptional = JsonUtil.getFieldValue(data, fieldPath);

            if (fieldValueOptional.isEmpty()) {
                log.debug("Field {} not found or null", fieldPath);
                return false;
            }

            return compareValues(conditionExpression.getOperator(), fieldValueOptional.get(),  expectedValue);

        } catch (Exception e) {
            log.error("Error evaluating condition {}: {}", conditionExpression, e.getMessage());
            return false;
        }
    }

    private static boolean compareValues(String operator, JsonNode actualValue, JsonNode expectedValue) {
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

    private static boolean compareEquals(JsonNode actualValue, JsonNode expectedValue) {
        if (actualValue.isNumber() && expectedValue.isNumber()) {
            return actualValue.asDouble() == expectedValue.asDouble();
        } else if (actualValue.isBoolean() && expectedValue.isBoolean()) {
            return actualValue.asBoolean() == expectedValue.asBoolean();
        } else {
            return actualValue.asString().equals(expectedValue.asString());
        }
    }

    private static boolean compareGreaterThan(JsonNode actualValue, JsonNode expectedValue) {
        if (actualValue.isNumber() && expectedValue.isNumber()) {
            return actualValue.asDouble() > expectedValue.asDouble();
        }
        return false;
    }

    private static boolean compareGreaterThanOrEqual(JsonNode actualValue, JsonNode expectedValue) {
        if (actualValue.isNumber() && expectedValue.isNumber()) {
            return actualValue.asDouble() >= expectedValue.asDouble();
        }
        return false;
    }

    private static boolean compareLessThan(JsonNode actualValue, JsonNode expectedValue) {
        if (actualValue.isNumber() && expectedValue.isNumber()) {
            return actualValue.asDouble() < expectedValue.asDouble();
        }
        return false;
    }

    private static boolean compareLessThanOrEqual(JsonNode actualValue, JsonNode expectedValue) {
        if (actualValue.isNumber() && expectedValue.isNumber()) {
            return actualValue.asDouble() <= expectedValue.asDouble();
        }
        return false;
    }
}
