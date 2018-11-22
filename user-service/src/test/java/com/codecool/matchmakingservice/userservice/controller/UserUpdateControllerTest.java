package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.model.User;
import com.codecool.matchmakingservice.userservice.model.UserStatus;
import com.codecool.matchmakingservice.userservice.repository.UserRepository;
import com.codecool.matchmakingservice.userservice.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

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

    @MockBean
    private UserService service;

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
        //todo mock the getUserFromJson method correctly for the unit tests instead of using it
        Mockito.when(service.getUserFromJson(Mockito.anyString())).thenCallRealMethod();
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
        mockMvc.perform(post("/user").content(adam.toJSonString())).andExpect(status().isBadRequest()).andExpect(content().string("Invalid user data."));
    }

    @Test
    public void postRequestNewUserIntoDatabaseWhenUserAlreadyInDatabaseRespondsBadRequest() throws Exception {
        Optional<User> result = Optional.of(adam);
        Mockito.when(repository.findByEmail("adam@mms.com")).thenReturn(result);

        mockMvc.perform(post("/user").content(adam.toJSonString())).andExpect(status().isBadRequest()).andExpect(content().string("Email address already in use."));
    }

    @Test
    public void postRequestNewUserIntoDatabaseWithInValidNameRespondsBadRequest() throws Exception {
        adam.setName(null);
        mockMvc.perform(post("/user").content(adam.toJSonString())).andExpect(status().isBadRequest()).andExpect(content().string("Invalid user data."));
    }

    @Test
    public void postRequestNewUserIntoDatabaseWithInValidPasswordRespondsBadRequest() throws Exception {
        adam.setPassword(null);
        mockMvc.perform(post("/user").content(adam.toJSonString())).andExpect(status().isBadRequest()).andExpect(content().string("Invalid user data."));
    }

    @Test
    public void postRequestNewUserIntoDatabaseWithInValidStatusRespondsBadRequest() throws Exception {
        adam.setStatus(UserStatus.DEFAULT);
        mockMvc.perform(post("/user").content(adam.toJSonString())).andExpect(status().isBadRequest()).andExpect(content().string("Invalid user data."));
    }

    @Test
    public void postRequestNewUserIntoDatabaseWithMissingIdRespondsOk() throws Exception {
        adam.setId(null);
        mockMvc.perform(post("/user").content(adam.toJSonString())).andExpect(status().isOk());
    }

    @Test
    public void postRequestNewUserIntoDatabaseWithMissingEloRespondsBadRequest() throws Exception {
        String userJson = "{'id':6, 'name':'bill', 'password':'billpass', 'email':'bill@mms.com'," +
            "'status':'OFFLINE'}";
        mockMvc.perform(post("/user").content(userJson)).andExpect(status().isBadRequest()).andExpect(content().string("Invalid user data."));
    }

    // todo check for validity of password (it should be a hash)

    @Test
    public void deleteRequestForInvalidIdRespondsBadRequest() throws Exception {
        mockMvc.perform(delete("/user/test")).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteRequestForUserThatIsNotInDatabaseResponseIsNotFound() throws Exception {
        Optional<User> result = Optional.empty();
        Mockito.when(repository.findById(999L)).thenReturn(result);
        mockMvc.perform(delete("/user/999")).andExpect(status().isNotFound());
    }

    @Test
    public void deleteRequestForUserThatIsInDatabaseResponseIsOk() throws Exception {
        Optional<User> result = Optional.of(adam);
        Mockito.when(repository.findById(1L)).thenReturn(result);
        mockMvc.perform(delete("/user/1")).andExpect(status().isOk());
    }

    @Test
    public void putRequestForInvalidIdRespondsBadRequest() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("userparam", "name");
        mockMvc.perform(put("/user/test").content("{}").params(params)).andExpect(status().isBadRequest());
    }

    @Test
    public void putRequestForUserThatIsNotInDatabaseRespondsNotFound() throws Exception {
        Optional<User> result = Optional.empty();
        Mockito.when(repository.findById(999L)).thenReturn(result);

        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("userparam", "name");
        mockMvc.perform(put("/user/999").content("{}").params(params)).andExpect(status().isNotFound());
    }

    @Test
    public void putRequestForValidUserWithEmptyBodyRespondsBadRequest() throws Exception {
        Optional<User> result = Optional.of(adam);
        Mockito.when(repository.findById(1L)).thenReturn(result);

        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("userparam", "name");
        mockMvc.perform(put("/user/1").content("").params(params)).andExpect(status().isBadRequest());
    }

    @Test
    public void putRequestForValidUserWithIdRespondsBadRequest() throws Exception {
        Optional<User> result = Optional.of(adam);
        Mockito.when(repository.findById(1L)).thenReturn(result);

        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("userparam", "id");
        mockMvc.perform(put("/user/1").content("{}").params(params)).andExpect(status().isBadRequest());
    }

    // todo put request for user/id (to update a given user's certain property)
    
}