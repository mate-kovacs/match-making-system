package com.codecool.matchmakingservice.userservice.service;

import com.codecool.matchmakingservice.userservice.model.InvalidUserParameterException;
import com.codecool.matchmakingservice.userservice.model.User;
import com.codecool.matchmakingservice.userservice.model.UserStatus;
import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User getUserFromJson(String userJson) throws RuntimeException {
        try {
            User user = new User();
            user.setId(Long.parseLong((JsonPath.parse(userJson).read("$.id")).toString()));
            user.setEmail(JsonPath.parse(userJson).read("$.email"));
            user.setPassword(JsonPath.parse(userJson).read("$.password"));
            user.setName(JsonPath.parse(userJson).read("$.name"));
            user.setElo(JsonPath.parse(userJson).read("$.elo"));
            String statusString = JsonPath.parse(userJson).read("$.status");
            UserStatus status = UserStatus.DEFAULT;
            for (UserStatus current : UserStatus.values()) {
                if (current.name().equals(statusString)) {
                    status = current;
                }
            }
            user.setStatus(status);
            return user;
        } catch (Exception ex) {
            throw new InvalidUserParameterException("Invalid user data.");
        }
    }
}
