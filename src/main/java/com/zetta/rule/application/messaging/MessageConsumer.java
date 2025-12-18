package com.zetta.rule.application.messaging;

import tools.jackson.databind.JsonNode;

public interface MessageConsumer {
    void consume(JsonNode message);
}
