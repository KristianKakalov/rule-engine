package com.zetta.rule.application.messaging;

import tools.jackson.databind.JsonNode;

public interface MessageProducer {
    void sendProcessed(JsonNode message);
    void sendError(JsonNode message);
}
