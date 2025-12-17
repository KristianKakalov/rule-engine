package com.zetta.rule.application.persistence.service;

import com.zetta.rule.application.persistence.entity.OrderEntity;
import com.zetta.rule.application.persistence.entity.UserEntity;
import com.zetta.rule.application.persistence.mapper.OrderMapper;
import com.zetta.rule.application.persistence.mapper.UserMapper;
import com.zetta.rule.application.persistence.repository.OrderRepository;
import com.zetta.rule.application.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

@Service
@AllArgsConstructor
public class DataPersistenceService {

    private static final Logger log = LoggerFactory.getLogger(DataPersistenceService.class);

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;

    @Transactional
    public void persistMessage(String messageId, JsonNode data) {
        try {

            UserEntity user = getOrCreateUser(data);
            userRepository.save(user);

            OrderEntity order = orderMapper.mapFromJson(data);
            user.addOrder(order);

            orderRepository.save(order);

            log.info("Successfully persisted message {} with user {} and orderId {}",
                    messageId, user.getUsername(), order.getId());

        } catch (Exception e) {
            log.error("Failed to persist message {}", messageId, e);
            throw new RuntimeException("Failed to persist data", e);
        }
    }

    private UserEntity getOrCreateUser(JsonNode data) {
        String username = data.has("user.username")
                ? data.get("user.username").asString()
                : data.get("user").get("username").asString();

        return userRepository.findByUsername(username)
                .map(existingUser -> {
                    userMapper.updateUserFromJson(existingUser, data);
                    return existingUser;
                })
                .orElseGet(() -> userMapper.mapFromJson(data));
    }
}
