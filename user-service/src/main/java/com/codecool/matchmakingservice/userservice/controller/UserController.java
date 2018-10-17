package com.codecool.matchmakingservice.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @GetMapping(path = "/user/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserById(@RequestParam("id") String id){
        String user = "{\"id\": 1}";
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/users/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserByName(@RequestParam("name") String name) {
        String userList = "{\"users\":[]}";
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(path = "/user/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserByEmail(@RequestParam("email") String email) {
        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
    }
}
