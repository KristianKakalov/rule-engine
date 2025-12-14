package com.zetta.ruleengine.config;

import com.zetta.ruleengine.engine.condition.ConditionGroup;
import com.zetta.ruleengine.engine.transformation.TransformationRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class RuleConfig {
    private static final Logger log = LoggerFactory.getLogger(RuleConfig.class);


    @Value("${app.rules.conditions-path}")
    private String conditionPath;
    @Value("${app.rules.transformations-path}")
    private String transformationPath;

    private final ObjectMapper objectMapper;

    public RuleConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public List<ConditionGroup> conditionsGroups() {
        try (InputStream is = Files.newInputStream(Paths.get(conditionPath))) {
            List<ConditionGroup> conditionGroupDefinitions = objectMapper.readValue(is, objectMapper
                    .getTypeFactory()
                    .constructCollectionType(List.class, ConditionGroup.class));
            log.info("Loaded conditions: {}", conditionGroupDefinitions);
            return conditionGroupDefinitions;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load conditions", e);
        }
    }

    @Bean
    public List<TransformationRule> transformationRules() {
        try (InputStream is = Files.newInputStream(Paths.get(transformationPath))) {
            List<TransformationRule> transformationRules = objectMapper.readValue(is, objectMapper
                    .getTypeFactory()
                    .constructCollectionType(List.class, TransformationRule.class));
            log.info("Loaded transformation rules: {}", transformationRules);
            return transformationRules;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load transformation rules", e);
        }
    }
}
