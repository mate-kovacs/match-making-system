package com.codecool.matchmakingservice.userservice.service;

import com.codecool.matchmakingservice.userservice.model.InvalidUserParameterException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    private UserService service;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setup() {
        service = new UserService();
    }

    @Test
    public void getUserFromJsonForNullParameterThrowsExceptionWithMessageInvalidInput() {
        exceptionRule.expect(InvalidUserParameterException.class);
        exceptionRule.expectMessage("Invalid user data.");
        service.getUserFromJson(null);
    }

    @Test
    public void getUserFromJsonForNotJsonStringParameterThrowsExceptionWithMessageInvalidInput() {
        exceptionRule.expect(InvalidUserParameterException.class);
        exceptionRule.expectMessage("Invalid user data.");
        service.getUserFromJson("{name:'Adam', pass");
    }
}