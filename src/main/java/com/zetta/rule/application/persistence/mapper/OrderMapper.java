package com.zetta.rule.application.persistence.mapper;

import com.zetta.rule.application.persistence.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.NullNode;

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
        JsonNode orderNode = data.has(ORDER_KEY) ? data.get(ORDER_KEY) : NullNode.getInstance();

        if (orderNode.has("amount") && orderNode.get("amount").isNumber()) {
            order.setAmount(orderNode.get("amount").asDecimal());
        } else if (data.has("order.amount") && data.get("order.amount").isNumber()) {
            order.setAmount(data.get("order.amount").asDecimal());
        }
        if (orderNode.has("totalWithTax") && orderNode.get("totalWithTax").isNumber()) {
            order.setTotalWithTax(orderNode.get("totalWithTax").asDecimal());
        } else if (data.has("order.totalWithTax") && data.get("order.totalWithTax").isNumber()) {
            order.setTotalWithTax(data.get("order.totalWithTax").asDecimal());
        }
    }
}
