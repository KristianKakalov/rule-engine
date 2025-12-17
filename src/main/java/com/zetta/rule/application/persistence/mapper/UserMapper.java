package com.zetta.rule.application.persistence.mapper;

import com.zetta.rule.application.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

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
        JsonNode userNode = data.has(USER_KEY) ? data.get(USER_KEY) : data;

        if (userNode.has("username")) user.setUsername(userNode.get("username").asString());
        if (userNode.has("fullName")) user.setFullName(userNode.get("fullName").asString());
        if (userNode.has("country")) user.setCountry(userNode.get("country").asString());
        if (userNode.has("age") && userNode.get("age").isInt())
            user.setAge(userNode.get("age").asInt());
        if (userNode.has("visits") && userNode.get("visits").isInt())
            user.setVisits(userNode.get("visits").asInt());
    }
}
