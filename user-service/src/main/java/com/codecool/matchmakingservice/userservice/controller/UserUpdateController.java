package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserUpdateController {

    @Autowired
    UserRepository repository;
}
