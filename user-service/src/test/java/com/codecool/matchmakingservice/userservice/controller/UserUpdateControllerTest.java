package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.model.User;
import com.codecool.matchmakingservice.userservice.model.UserStatus;
import com.codecool.matchmakingservice.userservice.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserUpdateController.class)
public class UserUpdateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repository;

    @InjectMocks
    private UserUpdateController controller;

    private User adam;
    private User cindy;
    private User wendy;
    private User invaLidi;

    @Before
    public void setup() {
        adam = new User();
        adam.setId(1L);
        adam.setName("Adam");
        adam.setEmail("adam@mms.com");
        adam.setPassword("password1");
        adam.setStatus(UserStatus.OFFLINE);
        adam.setElo(100);
        wendy = new User();
        wendy.setId(2L);
        wendy.setName("Wendy");
        wendy.setEmail("wendy@mms.com");
        wendy.setPassword("password2");
        wendy.setStatus(UserStatus.OFFLINE);
        wendy.setElo(125);
        cindy = new User();
        cindy.setId(3L);
        cindy.setName("Cindy");
        cindy.setEmail("cindy@mms.com");
        cindy.setPassword("password3");
        cindy.setStatus(UserStatus.ONLINE);
        cindy.setElo(150);
    }

    @Test
    public void postRequestNewUserIntoDatabaseWithMissingBodyRespondsBadRequestAndMissingDataMessage() throws Exception {
        mockMvc.perform(post("/user")).andExpect(status().isBadRequest());
    }

    @Test
    public void postRequestNewUserIntoDatabaseWithValidInputRespondsOk() throws Exception {
        mockMvc.perform(post("/user").content(adam.toJSonString())).andExpect(status().isOk());
    }

    @Test
    public void postRequestNewUserIntoDatabaseWithInValidEmailRespondsBadRequest() throws Exception {
        adam.setEmail("adam@mms.commm");
        mockMvc.perform(post("/user").content(adam.toJSonString())).andExpect(status().isBadRequest()).andExpect(content().string("Invalid email."));
    }

    @Test
    public void postRequestNewUserIntoDatabaseWhenUserAlreadyInDatabaseRespondsBadRequest() throws Exception {
        Optional<User> result = Optional.of(adam);
        Mockito.when(repository.findByEmail("adam@mms.com")).thenReturn(result);

        mockMvc.perform(post("/user").content(adam.toJSonString())).andExpect(status().isBadRequest()).andExpect(content().string("Email address already in use."));
    }

    // todo check for missing parameters (it should be handled by a different method that turns a json into a user)
    // todo check for validity of password (it should be a hash)

    // todo put request for user/id (to update a given user's certain property)
    
    // todo delete request for user/id (to delete a certain user - not that likely to be necessary)

}