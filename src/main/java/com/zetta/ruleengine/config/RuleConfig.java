package com.zetta.ruleengine.config;

import com.zetta.ruleengine.engine.condition.Condition;
import com.zetta.ruleengine.engine.condition.creator.ConditionFactory;
import com.zetta.ruleengine.engine.condition.LogicalOperation;
import com.zetta.ruleengine.engine.condition.dto.ConditionDto;
import com.zetta.ruleengine.engine.transformation.Transformation;
import com.zetta.ruleengine.engine.transformation.TransformationType;
import com.zetta.ruleengine.engine.transformation.creator.TransformationFactory;
import com.zetta.ruleengine.engine.transformation.dto.TransformationRule;
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
import java.util.LinkedList;
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
    public List<Condition> conditions() {
        try (InputStream is = Files.newInputStream(Paths.get(conditionPath))) {
            List<ConditionDto> conditionDtoList = objectMapper.readValue(is, objectMapper
                    .getTypeFactory()
                    .constructCollectionType(List.class, ConditionDto.class));

            List<Condition> conditions = new LinkedList<>();
            for (ConditionDto conditionDto : conditionDtoList) {
                if (!LogicalOperation.isValid(conditionDto.getLogicalOperation())) {
                    log.warn("Invalid logical operation - {}", conditionDto.getLogicalOperation());
                }
                conditions.add(ConditionFactory.create(conditionDto));
            }

            log.info("Loaded conditions: {}", conditionDtoList);
            return conditions;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load conditions", e);
        }
    }

    @Bean
    public List<Transformation> transformations() {
        try (InputStream is = Files.newInputStream(Paths.get(transformationPath))) {
            List<TransformationRule> transformationRules = objectMapper.readValue(is, objectMapper
                    .getTypeFactory()
                    .constructCollectionType(List.class, TransformationRule.class));

            List<Transformation> transformations = new LinkedList<>();
            for (TransformationRule rule : transformationRules) {
                if (!TransformationType.isValid(rule.getType())) {
                    log.warn("Invalid transformation operation - {}", rule.getType());
                }
                transformations.add(TransformationFactory.create(rule));
            }

            log.info("Loaded transformation rules: {}", transformations);
            return transformations;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load transformation rules", e);
        }
    }
}
