package com.codecool.matchmakingservice.userservice.service;

import com.codecool.matchmakingservice.userservice.model.InvalidUserDataException;
import com.codecool.matchmakingservice.userservice.model.User;
import com.codecool.matchmakingservice.userservice.model.UserStatus;
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

    private User adam;

    @Before
    public void setup() {
        service = new UserService();
        adam = new User();
        adam.setId(1L);
        adam.setName("Adam");
        adam.setEmail("adam@mms.com");
        adam.setPassword("password1");
        adam.setStatus(UserStatus.OFFLINE);
        adam.setElo(100);
    }

    @Test
    public void getUserFromJsonForNullParameterThrowsExceptionWithMessageInvalidInput() {
        exceptionRule.expect(InvalidUserDataException.class);
        exceptionRule.expectMessage("Invalid user data.");
        service.getUserFromJson(null);
    }

    @Test
    public void getUserFromJsonForNotJsonStringParameterThrowsExceptionWithMessageInvalidInput() {
        exceptionRule.expect(InvalidUserDataException.class);
        exceptionRule.expectMessage("Invalid user data.");
        service.getUserFromJson("{name:'Adam', pass");
    }

    @Test
    public void getUserFromJsonForValidParameterReturnsUser() {
        assertTrue(service.getUserFromJson(adam.toJSonString()).equals(adam));
    }

    @Test
    public void getUserFromJsonWithMissingIdReturnsUser() {
        adam.setId(null);
        assertTrue(service.getUserFromJson(adam.toJSonString()).getEmail().equals(adam.getEmail()));
    }

    @Test
    public void getUserFromJsonWithMissingNameThrowsException() {
        adam.setName(null);
        exceptionRule.expect(InvalidUserDataException.class);
        exceptionRule.expectMessage("Invalid user data.");
        service.getUserFromJson(adam.toJSonString());
    }

    @Test
    public void getUserFromJsonWithMissingEmailThrowsException() {
        adam.setEmail(null);
        exceptionRule.expect(InvalidUserDataException.class);
        exceptionRule.expectMessage("Invalid user data.");
        service.getUserFromJson(adam.toJSonString());
    }

    @Test
    public void getUserFromJsonWithInvalidEmailThrowsException() {
        adam.setEmail("adam@mms.commmmm");
        exceptionRule.expect(InvalidUserDataException.class);
        exceptionRule.expectMessage("Invalid user data.");
        service.getUserFromJson(adam.toJSonString());
    }

    @Test
    public void getUserFromJsonWithMissingPasswordThrowsException() {
        adam.setPassword(null);
        exceptionRule.expect(InvalidUserDataException.class);
        exceptionRule.expectMessage("Invalid user data.");
        service.getUserFromJson(adam.toJSonString());
    }

    @Test
    public void getUserFromJsonWithMissingStatusThrowsException() {
        adam.setStatus(null);
        exceptionRule.expect(InvalidUserDataException.class);
        exceptionRule.expectMessage("Invalid user data.");
        service.getUserFromJson(adam.toJSonString());
    }

    @Test
    public void getUserFromJsonWithMissingEloThrowsException() {
        String userJson = "{'id':6, 'name':'bill', 'password':'billpass', 'email':'bill@mms.com'," +
                "'status':'OFFLINE'}";
        exceptionRule.expect(InvalidUserDataException.class);
        exceptionRule.expectMessage("Invalid user data.");
        service.getUserFromJson(userJson);
    }

    //todo possible check for invalid password (it should be a hash)

    //todo possibly update the check for invalid parameters with more business logic (such as banned characters from user names, etc.)
}