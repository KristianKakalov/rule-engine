package com.zetta.rule.application.persistence.service;

import tools.jackson.databind.JsonNode;

public interface PersistenceService {
    void persistMessage(String messageId, JsonNode data);
}
