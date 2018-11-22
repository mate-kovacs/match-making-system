package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.model.InvalidUserDataException;
import com.codecool.matchmakingservice.userservice.model.User;
import com.codecool.matchmakingservice.userservice.repository.UserRepository;
import com.codecool.matchmakingservice.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.jayway.jsonpath.JsonPath;

import java.util.ArrayList;
import java.util.List;

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

    @DeleteMapping(path = "user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        Long userId;
        try {
            userId = Long.parseLong(id);
        } catch (NumberFormatException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (repository.findById(userId).isPresent()) {
            repository.deleteById(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "user/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") String id,
                                           @RequestBody String userJson,
                                           @RequestParam("userparam") String userProperty) {
        Long userId;
        List<String> validUserProperties = new ArrayList<>();
        validUserProperties.add("name");
        validUserProperties.add("password");
        validUserProperties.add("email");
        validUserProperties.add("elo");
        validUserProperties.add("status");
        try {
            userId = Long.parseLong(id);
        } catch (NumberFormatException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!repository.findById(userId).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (! validUserProperties.contains(userProperty)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
