package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.model.User;
import com.codecool.matchmakingservice.userservice.model.UserStatus;
import com.codecool.matchmakingservice.userservice.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jayway.jsonpath.JsonPath;

@Controller
public class UserUpdateController {

    @Autowired
    UserRepository repository;

    @PostMapping(path = "/user")
    public ResponseEntity<String> addNewUser(@RequestBody String userJson) {
        User user = new User();
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

        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            return new ResponseEntity<>("Invalid email.", HttpStatus.BAD_REQUEST);
        }
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email address already in use.", HttpStatus.BAD_REQUEST);
        }
        repository.save(user);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
