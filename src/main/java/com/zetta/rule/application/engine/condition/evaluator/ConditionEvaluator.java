package com.zetta.rule.application.engine.condition.evaluator;

import com.zetta.rule.application.engine.condition.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.List;

@Component
public class ConditionEvaluator implements Evaluator {
    private static final Logger log = LoggerFactory.getLogger(ConditionEvaluator.class);

    private final List<Condition> conditions;

    public ConditionEvaluator(List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean evaluate(JsonNode data) {
        for (Condition condition : conditions) {
            if (condition.evaluate(data)) {
                log.info("Matched {} conditions for {}", condition.getId(), data);
                return true;
            }
        }
        log.info("No matched conditions for {}", data);
        return false;
    }
}
