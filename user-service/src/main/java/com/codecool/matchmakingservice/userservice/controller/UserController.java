package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.repository.UserRepository;
import com.codecool.matchmakingservice.userservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserRepository repository;

    @GetMapping(path = "/user/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserById(@RequestParam("id") String id) {
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
        if (!EmailValidator.getInstance().isValid(email)) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping(path = "/users/elo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUserByElo(@RequestParam("min_elo") String minElo,
                                               @RequestParam("max_elo") String maxElo) {
        int smallestElo;
        int highestElo;
        try {
            smallestElo = Integer.parseInt(minElo);
            highestElo = Integer.parseInt(maxElo);
        } catch (NumberFormatException ex) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
        List<User> users = repository.findAllByEloBetweenOrderByIdAscEloAsc(smallestElo, highestElo);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
