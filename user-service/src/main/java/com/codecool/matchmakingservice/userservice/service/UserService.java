package com.codecool.matchmakingservice.userservice.service;

import com.codecool.matchmakingservice.userservice.model.InvalidUserParameterException;
import com.codecool.matchmakingservice.userservice.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User getUserFromJson(String userJson) throws RuntimeException {
        throw new InvalidUserParameterException("Invalid user data.");
    }
}
