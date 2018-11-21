package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.model.User;
import com.codecool.matchmakingservice.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserUpdateController {

    @Autowired
    UserRepository repository;

    @PostMapping(path = "/user")
    public ResponseEntity<String> addNewUser(@RequestBody String user) {
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
