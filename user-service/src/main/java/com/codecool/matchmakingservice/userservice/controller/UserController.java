package com.codecool.matchmakingservice.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

    @GetMapping(path = "/user/{id}", produces = "application/json")
    public ResponseEntity<String> getUserById(@PathVariable int id){
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
