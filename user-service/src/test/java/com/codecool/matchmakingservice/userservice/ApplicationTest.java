package com.codecool.matchmakingservice.userservice;

import com.codecool.matchmakingservice.userservice.controller.UserController;
import com.codecool.matchmakingservice.userservice.controller.UserUpdateController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserUpdateController userUpdateController;

    @Test
    public void checkSpringBootApplication(){
    }

    @Test
    public void checkIfUserControllerGetsCreated() {
        assertNotNull(userController);
    }

    @Test
    public void checkIfUserUpdateControllerGetsCreated() {
        assertNotNull(userUpdateController);
    }
}