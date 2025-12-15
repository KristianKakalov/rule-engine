package com.zetta.ruleengine.util;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonUtilTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testWhenRemoveTopLevelField() {
        // arrange
        JsonNode json = objectMapper.readTree("""
                {"name": "John", "age": 30}""");

        // act
        JsonNode result = JsonUtil.removeField(json, "age");

        // assert
        assertFalse(result.has("age"));
        assertTrue(result.has("name"));
        assertEquals("John", result.get("name").asString());
    }

    @Test
    void testWhenRemoveNestedField() {
        // arrange
        JsonNode json = objectMapper.readTree("""
                {"user": {"name": "John", "age": 30}}""");

        // act
        JsonNode result = JsonUtil.removeField(json, "user.age");

        // assert
        assertTrue(result.has("user"));
        assertTrue(result.get("user").has("name"));
        assertFalse(result.get("user").has("age"));
        assertEquals("John", result.get("user").get("name").asString());
    }

    @Test
    void testWhenUpdateFieldWithNewValue() {
        // arrange
        JsonNode json = objectMapper.readTree("""
                {"name": "John", "age": 30}""");

        // act
        JsonNode result = JsonUtil.updateField(json, "age", 35);

        // assert
        assertEquals(35, result.get("age").asInt());
        assertEquals("John", result.get("name").asString());
    }

    @Test
    void testWhenUpdateNestedField() {
        // arrange
        JsonNode json = objectMapper.readTree("""
                {"user": {"name": "John"}}""");

        // act
        JsonNode result = JsonUtil.updateField(json, "user.age", 30);

        // assert
        assertEquals("John", result.get("user").get("name").asString());
        assertEquals(30, result.get("user").get("age").asInt());
    }

    @Test
    void testWhenIncrementNumericField() {
        // arrange
        JsonNode json = objectMapper.readTree("""
                {"counter": 5}""");

        // act
        JsonNode result = JsonUtil.incrementField(json, "counter", 3);

        // assert
        assertEquals(8, result.get("counter").asInt());
    }

    @Test
    void testWhenGetFieldValueExists() {
        // arrange
        JsonNode json = objectMapper.readTree("""
                {"user": {"name": "John"}}""");

        // act
        Optional<JsonNode> result = JsonUtil.getFieldValue(json, "user.name");

        // assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().asString());
    }

    @Test
    void testWhenGetFieldValueDoesNotExist() {
        // arrange
        JsonNode json = objectMapper.readTree("""
                {"user": {"name": "John"}}""");

        // act
        Optional<JsonNode> result = JsonUtil.getFieldValue(json, "user.age");

        // assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testWhenIncrementNonNumericField() {
        // arrange
        JsonNode json = objectMapper.readTree("""
                {"name": "John"}""");

        // act
        JsonNode result = JsonUtil.incrementField(json, "name", 5);

        // assert
        assertEquals("John", result.get("name").asString());
    }
}