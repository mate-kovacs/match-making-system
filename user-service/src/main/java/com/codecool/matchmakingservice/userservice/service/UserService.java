package com.codecool.matchmakingservice.userservice.service;

import com.codecool.matchmakingservice.userservice.model.InvalidUserDataException;
import com.codecool.matchmakingservice.userservice.model.User;
import com.codecool.matchmakingservice.userservice.model.UserStatus;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User getUserFromJson(String userJson) throws RuntimeException {
        try {
            User user = new User();
            try {
                user.setId(Long.parseLong((JsonPath.parse(userJson).read("$.id")).toString()));
            } catch (NullPointerException ex) {
                user.setId(null);
            }
            user.setEmail(checkEmail(JsonPath.parse(userJson).read("$.email")));
            user.setPassword(checkPassword(JsonPath.parse(userJson).read("$.password")));
            user.setName(checkName(JsonPath.parse(userJson).read("$.name")));
            user.setElo(JsonPath.parse(userJson).read("$.elo"));
            String statusString = JsonPath.parse(userJson).read("$.status");
            UserStatus status = UserStatus.DEFAULT;
            for (UserStatus current : UserStatus.values()) {
                if (current.name().equals(statusString)) {
                    status = current;
                    break;
                }
            }
            if (status == UserStatus.DEFAULT) {
                throw new InvalidJsonException();
            } else {
                user.setStatus(status);
            }
            return user;
        } catch (PathNotFoundException | IllegalArgumentException | InvalidJsonException ex) {
            throw new InvalidUserDataException("Invalid user data.");
        }
    }

    private String checkPassword(String password) {
        if (password.equals("null")) {
            throw new InvalidJsonException();
        }
        return password;
    }

    private String checkEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new InvalidJsonException();
        }
        return email;
    }

    private String checkName(String name) {
        if (name.equals("null")) {
            throw new InvalidJsonException();
        }
        return name;
    }

    public boolean updateUserProperty(String userJson, String userProperty, Long userId, User user) {
        try {
            switch (userProperty) {
                case "name":
                    user.setName(checkName(JsonPath.parse(userJson).read("$.name")));
                    break;
                case "password":
                    user.setPassword(checkPassword(JsonPath.parse(userJson).read("$.password")));
                    break;
                case "email":
                    user.setEmail(checkEmail(JsonPath.parse(userJson).read("$.email")));
                    break;
                case "elo":
                    user.setElo(Integer.parseInt((JsonPath.parse(userJson).read("$.elo")).toString()));
                    break;
                case "status":
                    String statusString = JsonPath.parse(userJson).read("$.status");
                    UserStatus status = UserStatus.DEFAULT;
                    for (UserStatus current : UserStatus.values()) {
                        if (current.name().equals(statusString)) {
                            status = current;
                            break;
                        }
                    }
                    if (status == UserStatus.DEFAULT) {
                        throw new InvalidJsonException();
                    } else {
                        user.setStatus(status);
                    }
                    break;
                default:
                    return false;
            }
        } catch (PathNotFoundException | InvalidJsonException | NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
