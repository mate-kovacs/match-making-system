package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserUpdateController {

    @Autowired
    UserRepository repository;

    @PostMapping(path = "/user")
    public ResponseEntity<String> addNewUser() {
        String message = "Data is missing.";
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
