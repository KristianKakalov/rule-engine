package com.zetta.rule.application.engine.condition.evaluator.util;

import com.zetta.rule.application.engine.condition.dto.ConditionExpression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

class ConditionExpressionEvaluatorUtilTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testEvaluateGreaterThanCondition() {
        //arrange
        JsonNode data = objectMapper.readTree("""
                {"user": {"age": 25}}""");

        ConditionExpression expression = new ConditionExpression(
                "user.age",
                ">",
                objectMapper.readTree("18")
        );

        //act
        boolean result = ConditionExpressionEvaluatorUtil.evaluateCondition(expression, data);

        //assert
        Assertions.assertTrue(result);
    }

    @Test
    void testWhenFieldIsMissing() {
        //arrange
        JsonNode data = objectMapper.readTree("{}");

        ConditionExpression expression = new ConditionExpression(
                "user.age",
                ">",
                objectMapper.readTree("18")
        );

        //act
        boolean result = ConditionExpressionEvaluatorUtil.evaluateCondition(expression, data);

        //assert
        Assertions.assertFalse(result);
    }

}