package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.model.InvalidUserDataException;
import com.codecool.matchmakingservice.userservice.model.User;
import com.codecool.matchmakingservice.userservice.model.UserStatus;
import com.codecool.matchmakingservice.userservice.repository.UserRepository;
import com.codecool.matchmakingservice.userservice.service.UserService;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.PathNotFoundException;
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
        try {
            userId = Long.parseLong(id);
        } catch (NumberFormatException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!repository.findById(userId).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return updateUserProperty(userJson, userProperty, userId);
    }

    private ResponseEntity<Void> updateUserProperty(@RequestBody String userJson, @RequestParam("userparam") String userProperty, Long userId) {
        User user = repository.findById(userId).get();
        try {
            switch (userProperty) {
                case "name":
                    user.setName(service.checkName(JsonPath.parse(userJson).read("$.name")));
                    break;
                case "password":
                    user.setPassword(service.checkPassword(JsonPath.parse(userJson).read("$.password")));
                    break;
                case "email":
                    user.setEmail(service.checkEmail(JsonPath.parse(userJson).read("$.email")));
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
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (PathNotFoundException | InvalidJsonException | NumberFormatException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
