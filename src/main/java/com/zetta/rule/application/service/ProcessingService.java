package com.zetta.rule.application.service;

import tools.jackson.databind.JsonNode;

public interface ProcessingService {
    void process(JsonNode rawMessage);
}
