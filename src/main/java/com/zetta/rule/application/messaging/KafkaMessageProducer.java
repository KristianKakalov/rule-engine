package com.zetta.rule.application.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class KafkaMessageProducer implements MessageProducer {

    private final KafkaTemplate<String, JsonNode> kafkaTemplate;
    private final String outputTopic;
    private final String errorTopic;

    public KafkaMessageProducer(KafkaTemplate<String, JsonNode> kafkaTemplate,
                                @Value("${app.kafka.output-topic}") String outputTopic,
                                @Value("${app.kafka.error-topic}") String errorTopic) {

        this.kafkaTemplate = kafkaTemplate;
        this.outputTopic = outputTopic;
        this.errorTopic = errorTopic;
    }

    @Override
    public void sendProcessed(JsonNode message) {
        kafkaTemplate.send(outputTopic, message);
    }

    @Override
    public void sendError(JsonNode message) {
        kafkaTemplate.send(errorTopic, message);
    }
}
