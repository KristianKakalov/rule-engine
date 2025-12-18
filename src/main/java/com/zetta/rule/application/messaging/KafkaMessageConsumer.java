package com.zetta.rule.application.messaging;

import com.zetta.rule.application.service.ProcessingService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
@AllArgsConstructor
public class KafkaMessageConsumer implements MessageConsumer {

    private final ProcessingService processingService;

    @Override
    @KafkaListener(topics = "${app.kafka.input-topic}")
    public void consume(JsonNode message) {
        processingService.process(message);
    }
}
