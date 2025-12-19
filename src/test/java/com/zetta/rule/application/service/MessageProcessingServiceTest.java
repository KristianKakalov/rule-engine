package com.zetta.rule.application.service;

import com.zetta.rule.application.engine.condition.evaluator.Evaluator;
import com.zetta.rule.application.engine.transformation.engine.Transformer;
import com.zetta.rule.application.messaging.MessageProducer;
import com.zetta.rule.application.persistence.service.PersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageProcessingServiceTest {

    @Mock
    private Evaluator conditionEvaluator;

    @Mock
    private Transformer transformationEngine;

    @Mock
    private PersistenceService persistenceService;

    @Mock
    private MessageProducer producer;

    @InjectMocks
    private MessageProcessingService service;

    private JsonNode rawMessage;
    private JsonNode transformedMessage;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        rawMessage = objectMapper.readTree("""
            { "key": "value" }
        """);
        transformedMessage = objectMapper.readTree("""
            { "key": "transformedValue" }
        """);
    }

    @Test
    void testProcessMessageWhenConditionsMetPersistsAndSendsProcessed() {
        // arrange
        when(conditionEvaluator.evaluate(rawMessage)).thenReturn(true);
        when(transformationEngine.transform(rawMessage))
                .thenReturn(transformedMessage);

        // act
        service.process(rawMessage);

        // assert
        verify(conditionEvaluator).evaluate(rawMessage);
        verify(transformationEngine).transform(rawMessage);
        verify(persistenceService).persistMessage(transformedMessage);
        verify(producer).sendProcessed(transformedMessage);
        verify(producer, never()).sendError(any());
    }

    @Test
    void testProcessMessageWhenConditionsNotMetSendsErrorAndStopsProcessing() {
        // arrange
        when(conditionEvaluator.evaluate(rawMessage)).thenReturn(false);

        // act
        service.process(rawMessage);

        // assert
        verify(conditionEvaluator).evaluate(rawMessage);
        verify(producer).sendError(rawMessage);
        verify(transformationEngine, never()).transform(any());
        verify(persistenceService, never()).persistMessage(any());
        verify(producer, never()).sendProcessed(any());
    }

    @Test
    void testProcessMessageWhenExceptionThrownSendsErrorMessage() {
        // arrange
        when(conditionEvaluator.evaluate(rawMessage))
                .thenThrow(new RuntimeException("Evaluation failed"));

        // act
        service.process(rawMessage);

        // assert
        verify(producer).sendError(rawMessage);
        verify(persistenceService, never()).persistMessage(any());
        verify(producer, never()).sendProcessed(any());
    }
}
