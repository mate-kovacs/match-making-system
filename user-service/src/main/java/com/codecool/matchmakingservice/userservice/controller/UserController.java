package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.model.UserStatus;
import com.codecool.matchmakingservice.userservice.repository.UserRepository;
import com.codecool.matchmakingservice.userservice.model.User;
import org.hibernate.engine.spi.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserRepository repository;

    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers(@RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "email", required = false) String email,
                                               @RequestParam(value = "min_elo", required = false) String minElo,
                                               @RequestParam(value = "max_elo", required = false) String maxElo,
                                               @RequestParam(value = "status", required = false) String status) {
        if (!(email == null)) {
            return handleRequestByEmail(email);
        } else if (!(minElo == null && maxElo == null)) {
            return handleRequestByElo(minElo, maxElo);
        } else if (! (name == null)) {
            return handleRequestByName(name.toLowerCase());
        } else if (! (status == null)) {
            return handleRequestByStatus(status);
        } else {
            List<User> userList = repository.findAll();
            return new ResponseEntity<>(userList, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
        Long userId;
        try {
            userId = Long.parseLong(id);
        } catch (NumberFormatException ex) {
            return new ResponseEntity<>(new User(), HttpStatus.BAD_REQUEST);
        }
        if (userId <= 0) {
            return new ResponseEntity<>(new User(), HttpStatus.BAD_REQUEST);
        }
        Optional<User> optionalUser = repository.findById(userId);
        if (optionalUser.isPresent()) {
            return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new User(), HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<List<User>> handleRequestByElo(String minElo, String maxElo) {
        if (!(minElo == null || maxElo == null)) {
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
        } else {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<List<User>> handleRequestByEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        } else {
            List<User> users = new ArrayList<>();
            Optional<User> optionalUser = repository.findByEmail(email);
            if (optionalUser.isPresent()) {
                users.add(optionalUser.get());
            } else {
                return new ResponseEntity<>(users, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    private ResponseEntity<List<User>> handleRequestByName(String name) {
        List<User> users = repository.findAllByNameContainingIgnoreCaseOrderByIdAscNameAsc(name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    private ResponseEntity<List<User>> handleRequestByStatus(String statusName) {
        UserStatus status = UserStatus.DEFAULT;
        for (UserStatus current: UserStatus.values()) {
            if (current.name().equals(statusName)) {
                status = current;
            }
        }
        if (status == UserStatus.DEFAULT) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
        List<User> users = repository.findAllByStatusOrderByIdAscNameAsc(status);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
