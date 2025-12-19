package com.zetta.rule.application.persistence.service;

import com.zetta.rule.application.persistence.entity.OrderEntity;
import com.zetta.rule.application.persistence.entity.UserEntity;
import com.zetta.rule.application.persistence.mapper.OrderMapper;
import com.zetta.rule.application.persistence.mapper.UserMapper;
import com.zetta.rule.application.persistence.repository.OrderRepository;
import com.zetta.rule.application.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataPersistenceServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private DataPersistenceService service;

    private JsonNode jsonNode;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree("""
                    {
                      "user": {
                        "username": "john",
                        "fullName": "John Doe"
                      },
                      "order": {
                        "amount": 100,
                        "totalWithTax": 120
                      }
                    }
                """);
    }

    @Test
    void testPersistMessageExistingUserUpdatesAndSavesOrder() {
        // arrange
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("john");

        OrderEntity order = new OrderEntity();

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(existingUser));
        when(orderMapper.mapFromJson(jsonNode))
                .thenReturn(order);

        // act
        service.persistMessage(jsonNode);

        // assert
        verify(userMapper).updateUserFromJson(existingUser, jsonNode);
        verify(userRepository).save(existingUser);
        verify(orderMapper).mapFromJson(jsonNode);
        verify(orderRepository).save(order);
        assertTrue(existingUser.getOrders().contains(order));
    }

    @Test
    void testPersistMessageNewUserCreatesAndSavesUser() {
        // arrange
        UserEntity newUser = new UserEntity();
        newUser.setUsername("john");

        OrderEntity order = new OrderEntity();

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.empty());
        when(userMapper.mapFromJson(jsonNode))
                .thenReturn(newUser);
        when(orderMapper.mapFromJson(jsonNode))
                .thenReturn(order);

        // act
        service.persistMessage(jsonNode);

        // assert
        verify(userMapper).mapFromJson(jsonNode);
        verify(userRepository).save(newUser);
        verify(orderRepository).save(order);
        assertEquals(1, newUser.getOrders().size());
        assertEquals(order, newUser.getOrders().get(0));
    }

    @Test
    void testPersistMessageWhenExceptionThrown() {
        // arrange
        when(userRepository.findByUsername(anyString()))
                .thenThrow(new RuntimeException("DB down"));

        // act & assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.persistMessage(jsonNode));

        assertEquals("Failed to persist data", ex.getMessage());
        assertNotNull(ex.getCause());
        verify(orderRepository, never()).save(any());
        verify(userMapper, never()).mapFromJson(any());
    }
}
