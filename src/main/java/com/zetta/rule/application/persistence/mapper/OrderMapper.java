package com.zetta.rule.application.persistence.mapper;

import com.zetta.rule.application.persistence.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
@RequiredArgsConstructor

public class OrderMapper {
    private static final String ORDER_KEY = "order";

    public OrderEntity mapFromJson(JsonNode data) {
        OrderEntity order = new OrderEntity();
        updateOrderFromJson(order, data);
        return order;
    }

    public void updateOrderFromJson(OrderEntity order, JsonNode data) {
        JsonNode orderNode = data.has(ORDER_KEY) ? data.get(ORDER_KEY) : data;

        if (orderNode.has("amount") && orderNode.get("amount").isNumber())
            order.setAmount(orderNode.get("amount").asDecimal());
        if (orderNode.has("totalWithTax") && orderNode.get("totalWithTax").isNumber())
            order.setTotalWithTax(orderNode.get("totalWithTax").asDecimal());
    }
}
