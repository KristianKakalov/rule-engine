package com.zetta.rule.application.persistence.mapper;

import com.zetta.rule.application.persistence.entity.OrderEntity;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderMapperTest {

    private final OrderMapper orderMapper = new OrderMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testMapFromJsonNestedOrderFields() {
        // arrange
        JsonNode jsonNode = objectMapper.readTree("""
            {
              "order": {
                "amount": 100.50,
                "totalWithTax": 120.75
              }
            }
        """);

        // act
        OrderEntity order = orderMapper.mapFromJson(jsonNode);

        // assert
        assertEquals(new BigDecimal("100.5"), order.getAmount());
        assertEquals(new BigDecimal("120.75"), order.getTotalWithTax());
    }

    @Test
    void testMapFromJsonDottedFields() {
        // arrange
        JsonNode jsonNode = objectMapper.readTree("""
            {
              "order.amount": 200,
              "order.totalWithTax": 240
            }
        """);

        // act
        OrderEntity order = orderMapper.mapFromJson(jsonNode);

        // assert
        assertEquals(new BigDecimal("200"), order.getAmount());
        assertEquals(new BigDecimal("240"), order.getTotalWithTax());
    }

    @Test
    void testUpdateOrderFromJsonUpdatesOnlyProvidedFields() {
        // arrange
        OrderEntity order = new OrderEntity();
        order.setAmount(new BigDecimal("50"));
        order.setTotalWithTax(new BigDecimal("60"));

        JsonNode jsonNode = objectMapper.readTree("""
            {
              "order": {
                "amount": 75
              }
            }
        """);

        // act
        orderMapper.updateOrderFromJson(order, jsonNode);

        // assert
        assertEquals(new BigDecimal("75"), order.getAmount());
        assertEquals(new BigDecimal("60"), order.getTotalWithTax());
    }
}
