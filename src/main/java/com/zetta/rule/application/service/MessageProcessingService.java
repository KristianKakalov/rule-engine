package com.zetta.rule.application.service;

import com.zetta.rule.application.engine.condition.evaluator.Evaluator;
import com.zetta.rule.application.engine.transformation.engine.Transformer;
import com.zetta.rule.application.messaging.MessageProducer;
import com.zetta.rule.application.persistence.service.PersistenceService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

@Service
@AllArgsConstructor
public class MessageProcessingService implements ProcessingService {

    private static final Logger log = LoggerFactory.getLogger(MessageProcessingService.class);

    private final Evaluator conditionEvaluator;
    private final Transformer transformationEngine;
    private final PersistenceService repository;
    private final MessageProducer producer;

    @Override
    public void process(JsonNode rawMessage) {
        try {
            if (!conditionEvaluator.evaluate(rawMessage)) {
                producer.sendError(rawMessage);
                log.info("Unmet conditions. Message send to error queue. Message: {}", rawMessage);
                return;
            }

            JsonNode transformed = transformationEngine.transform(rawMessage);

            repository.persistMessage(transformed);

            producer.sendProcessed(transformed);
            log.info("Message send to consumer queue. Message: {}", rawMessage);

        } catch (Exception e) {
            log.error("Error while processing the message.", e);
            log.info("Message send to error queue. Message: {}", rawMessage);
            producer.sendError(rawMessage);
        }
    }
}
