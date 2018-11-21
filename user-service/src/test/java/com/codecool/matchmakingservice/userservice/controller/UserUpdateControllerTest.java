package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.repository.UserRepository;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserUpdateController.class)
public class UserUpdateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repository;

    @InjectMocks
    private UserUpdateController controller;

    

}