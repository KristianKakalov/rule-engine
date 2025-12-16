package com.zetta.rule.application.engine.condition.evaluator;

import com.zetta.rule.application.engine.condition.AllConditions;
import com.zetta.rule.application.engine.condition.AnyConditions;
import com.zetta.rule.application.engine.condition.Condition;
import com.zetta.rule.application.engine.condition.NoneConditions;
import com.zetta.rule.application.engine.condition.dto.ConditionExpression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

class ConditionEvaluatorTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testWhenAllConditionsGroupMatches() {
        // arrange
        JsonNode data = objectMapper.readTree("""
                {"age": 25, "balance": 150}""");

        Condition condition = new AllConditions("adult-rich", List.of(
                new ConditionExpression("age", ">=", objectMapper.readTree("18")),
                new ConditionExpression("balance", ">", objectMapper.readTree("100"))
        ));

        ConditionEvaluator evaluator = new ConditionEvaluator(List.of(condition));

        // act
        boolean result = evaluator.evaluate(data);

        // assert
        Assertions.assertTrue(result);
    }

    @Test
    void testWhenAllConditionsGroupDoesNotMatch() {
        // arrange
        JsonNode data = objectMapper.readTree("""
                {"age": 25, "balance": 50}""");

        Condition condition = new AllConditions("adult-rich", List.of(
                new ConditionExpression("age", ">=", objectMapper.readTree("18")),
                new ConditionExpression("balance", ">", objectMapper.readTree("100"))
        ));

        ConditionEvaluator evaluator = new ConditionEvaluator(List.of(condition));

        // act
        boolean result = evaluator.evaluate(data);

        // assert
        Assertions.assertFalse(result);
    }

    @Test
    void testWhenAnyConditionsGroupMatches() {
        // arrange
        JsonNode data = objectMapper.readTree("""
                {"isVip": false, "purchaseCount": 15}""");

        Condition condition = new AnyConditions("vip-or-frequent", List.of(
                new ConditionExpression("isVip", "==", objectMapper.readTree("true")),
                new ConditionExpression("purchaseCount", ">", objectMapper.readTree("10"))
        ));

        ConditionEvaluator evaluator = new ConditionEvaluator(List.of(condition));

        // act
        boolean result = evaluator.evaluate(data);

        // assert
        Assertions.assertTrue(result);
    }

    @Test
    void testWhenAnyConditionsGroupDoesNotMatch() {
        // arrange
        JsonNode data = objectMapper.readTree("""
                {"isVip": false, "purchaseCount": 5}""");

        Condition condition = new AnyConditions("vip-or-frequent", List.of(
                new ConditionExpression("isVip", "==", objectMapper.readTree("true")),
                new ConditionExpression("purchaseCount", ">", objectMapper.readTree("10"))
        ));

        ConditionEvaluator evaluator = new ConditionEvaluator(List.of(condition));

        // act
        boolean result = evaluator.evaluate(data);

        // assert
        Assertions.assertFalse(result);
    }

    @Test
    void testWhenNoneConditionsGroupMatches() {
        // arrange
        JsonNode data = objectMapper.readTree("""
                {"country": "Greece"}
                """);

        Condition condition = new NoneConditions("unsupported-country", List.of(
                new ConditionExpression("country", "equals", objectMapper.readTree("\"Greece\"")),
                new ConditionExpression("username", "isEmpty",objectMapper.readTree("true"))
        ));

        ConditionEvaluator evaluator = new ConditionEvaluator(List.of(condition));

        // act
        boolean result = evaluator.evaluate(data);

        // assert
        Assertions.assertFalse(result);
    }

    @Test
    void testWhenMultipleConditionGroupsFirstMatches() {
        // arrange
        JsonNode data = objectMapper.readTree("""
                {"age": 25, "isVip": false}""");

        Condition condition1 = new AllConditions("adult", List.of(
                new ConditionExpression("age", ">=", objectMapper.readTree("18"))
        ));

        Condition condition2 = new AnyConditions("vip", List.of(
                new ConditionExpression("isVip", "==", objectMapper.readTree("true"))
        ));

        ConditionEvaluator evaluator = new ConditionEvaluator(List.of(condition1, condition2));

        // act
        boolean result = evaluator.evaluate(data);

        // assert
        Assertions.assertTrue(result);
    }
}