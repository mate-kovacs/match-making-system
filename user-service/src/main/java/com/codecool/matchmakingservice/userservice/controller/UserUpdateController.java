package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.model.InvalidUserDataException;
import com.codecool.matchmakingservice.userservice.model.User;
import com.codecool.matchmakingservice.userservice.repository.UserRepository;
import com.codecool.matchmakingservice.userservice.service.UserService;
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

    @Autowired
    UserService service;

    @PostMapping(path = "/user")
    public ResponseEntity<String> addNewUser(@RequestBody String userJson) {
        User user = new User();
        try {
            user = service.getUserFromJson(userJson);
        } catch (InvalidUserDataException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email address already in use.", HttpStatus.BAD_REQUEST);
        }
        repository.save(user);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
