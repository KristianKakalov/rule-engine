package com.zetta.ruleengine.engine.condition;

import org.junit.jupiter.api.Assertions;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ConditionEvaluatorTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnTrueWhenAllConditionsMatch() {
        // given
        JsonNode data = objectMapper.readTree("""
                    {
                      "user": { "age": 25 },
                      "orderAmount": 150
                    }
                """);

        ConditionGroup group = new ConditionGroup(
                "all-match",
                "all",
                List.of(
                        new Condition("user.age", ">=", objectMapper.readTree("18")),
                        new Condition("orderAmount", ">", objectMapper.readTree("100"))
                )
        );

        ConditionEvaluator evaluator = new ConditionEvaluator(List.of(group));

        // when
        boolean result = evaluator.evaluate(data);

        // then
        Assertions.assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenAnyConditionMatches() {
        // given
        JsonNode data = objectMapper.readTree("""
                    {
                      "user": { "age": 16 },
                      "orderAmount": 600
                    }
                """);

        ConditionGroup group = new ConditionGroup(
                "any-match",
                "any",
                List.of(
                        new Condition("user.age", ">=", objectMapper.readTree("18")),
                        new Condition("orderAmount", ">", objectMapper.readTree("500"))
                )
        );

        ConditionEvaluator evaluator = new ConditionEvaluator(List.of(group));

        // when
        boolean result = evaluator.evaluate(data);

        // then
        Assertions.assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenNoneConditionsMatch() {
        // given
        JsonNode data = objectMapper.readTree("""
                    {
                      "user": { "age": 15 },
                      "orderAmount": 50
                    }
                """);

        ConditionGroup group = new ConditionGroup(
                "none-match",
                "none",
                List.of(
                        new Condition("user.age", ">=", objectMapper.readTree("18")),
                        new Condition("orderAmount", ">", objectMapper.readTree("100"))
                )
        );

        ConditionEvaluator evaluator = new ConditionEvaluator(List.of(group));

        // when
        boolean result = evaluator.evaluate(data);

        // then
        Assertions.assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenNoLogicalOperationMatch() {
        // given
        JsonNode data = objectMapper.readTree("""
                    {
                      "user": { "age": 15 },
                      "orderAmount": 50
                    }
                """);

        ConditionGroup group = new ConditionGroup(
                "all-match",
                "all",
                List.of(
                        new Condition("user.age", "?", objectMapper.readTree("18"))
                )
        );

        ConditionEvaluator evaluator = new ConditionEvaluator(List.of(group));

        // when
        boolean result = evaluator.evaluate(data);

        // then
        Assertions.assertFalse(result);
    }
}
