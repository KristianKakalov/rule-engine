package com.zetta.rule.application.persistence.mapper;

import com.zetta.rule.application.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.NullNode;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private static final String USER_KEY = "user";

    public UserEntity mapFromJson(JsonNode data) {
        UserEntity user = new UserEntity();
        updateUserFromJson(user, data);
        return user;
    }

    public void updateUserFromJson(UserEntity user, JsonNode data) {
        JsonNode userNode = data.has(USER_KEY) ? data.get(USER_KEY) : NullNode.getInstance();

        if (userNode.has("username")) user.setUsername(userNode.get("username").asString());
        else if (data.has("user.username")) user.setUsername(data.get("user.username").asString());

        if (userNode.has("fullName")) user.setFullName(userNode.get("fullName").asString());
        else if (data.has("user.fullName")) user.setFullName(data.get("user.fullName").asString());

        if (userNode.has("country")) user.setCountry(userNode.get("country").asString());
        else if (data.has("user.country")) user.setCountry(data.get("user.country").asString());

        if (userNode.has("age") && userNode.get("age").isInt())
            user.setAge(userNode.get("age").asInt());
        else if (data.has("user.age") && data.get("user.age").isInt())
            user.setAge(data.get("user.age").asInt());

        if (userNode.has("visits") && userNode.get("visits").isInt())
            user.setVisits(userNode.get("visits").asInt());
        if (data.has("user.visits") && data.get("user.visits").isInt())
            user.setVisits(data.get("user.visits").asInt());
    }
}
