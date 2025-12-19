package com.zetta.rule.application.engine.transformation.engine;

import com.zetta.rule.application.engine.transformation.IncrementTransformation;
import com.zetta.rule.application.engine.transformation.RemoveTransformation;
import com.zetta.rule.application.engine.transformation.SetTransformation;
import com.zetta.rule.application.engine.transformation.Transformation;
import com.zetta.rule.application.engine.transformation.expression.AddExpressionTransformation;
import com.zetta.rule.application.engine.transformation.expression.ConcatExpressionTransformation;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransformationEngineTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testEngineWithSetTransformationWithConcatExpression() {
        // arrange
        JsonNode input = mapper.readTree("""
                    {
                      "user": {
                        "firstName": "John",
                        "lastName": "Doe"
                      }
                    }
                """);

        Transformation concat = new SetTransformation("user.fullName",
                new ConcatExpressionTransformation(
                        mapper.readTree("""
                                ["user.firstName", " ", "user.lastName"]""")));

        TransformationEngine engine =
                new TransformationEngine(List.of(concat));

        // act
        JsonNode result = engine.transform(input);

        // assert
        assertEquals("John Doe", result.at("/user/fullName").asString());
    }

    @Test
    void testEngineWithIncrementTransformation() {
        // arrange
        JsonNode input = mapper.readTree("""
                    {
                      "user": {
                        "age": 30
                      }
                    }
                """);

        Transformation increment =
                new IncrementTransformation("user.age", mapper.readTree("1"));

        TransformationEngine engine =
                new TransformationEngine(List.of(increment));

        // act
        JsonNode result = engine.transform(input);

        // assert
        assertEquals(31, result.at("/user/age").asInt());
    }

    @Test
    void testEngineWithRemoveTransformation() {
        // arrange
        JsonNode input = mapper.readTree("""
                    {
                      "user": {
                        "firstName": "John",
                        "lastName": "Doe"
                      }
                    }
                """);

        Transformation remove =
                new RemoveTransformation("user.lastName");

        TransformationEngine engine =
                new TransformationEngine(List.of(remove));

        // act
        JsonNode result = engine.transform(input);

        // assert
        assertTrue(result.at("/user/lastName").isMissingNode());
    }

    @Test
    void testEngineWithAddExpressionTransformation() {
        // arrange
        JsonNode input = mapper.readTree("""
                    {
                      "order": {
                        "amount": 90
                      }
                    }
                """);

        Transformation add = new SetTransformation("order.totalWithTax",
                new AddExpressionTransformation(
                        mapper.readTree("""
                                ["order.amount", 10]""")));

        TransformationEngine engine =
                new TransformationEngine(List.of(add));

        // act
        JsonNode result = engine.transform(input);

        // assert
        assertEquals(100, result.at("/order/totalWithTax").asInt());
    }

    @Test
    void testEngineWithMultipleTransformations() {
        // arrange
        JsonNode input = mapper.readTree("""
                    {
                      "user": {
                        "firstName": "Jane",
                        "lastName": "Doe",
                        "age": 25
                      }
                    }
                """);

        Transformation concat =
                new SetTransformation("user.fullName",
                        new ConcatExpressionTransformation(
                                mapper.readTree("""
                                        ["Mr. ", "user.firstName", " ", "user.lastName"]""")));

        Transformation increment = new IncrementTransformation("user.age", mapper.readTree("1"));
        Transformation remove = new RemoveTransformation("user.lastName");

        TransformationEngine engine = new TransformationEngine(List.of(concat, increment, remove));

        // act
        JsonNode result = engine.transform(input);

        // assert
        assertEquals("Mr. Jane Doe", result.at("/user/fullName").asString());
        assertEquals(26, result.at("/user/age").asInt());
        assertTrue(result.at("/user/lastName").isMissingNode());
    }
}