package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.model.User;
import com.codecool.matchmakingservice.userservice.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repository;

    @InjectMocks
    private UserController controller;

    private User adam;
    private User cindy;

    @Before
    public void setup() {
        adam = new User();
        adam.setId(1L);
        adam.setName("Adam");
        adam.setEmail("adam@mms.com");
        adam.setPassword("password1");
        adam.setElo(100);
        cindy = new User();
        cindy.setId(3L);
        cindy.setName("Cindy");
        cindy.setEmail("cindy@mms.com");
        cindy.setPassword("password3");
        cindy.setElo(150);
    }

    @Test
    public void wrongURLRespondsWithNotFound() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isNotFound());
    }

    @Test
    public void requestForUserByNegativeIdRespondsBadRequest() throws Exception{
        mockMvc.perform(get("/user/id").param("id", "-15")).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByZeroIdRespondsBadRequest() throws Exception{
        mockMvc.perform(get("/user/id").param("id", "0")).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByIdResponseTypeIsJsonWithUTF8Charset() throws Exception {
        mockMvc.perform(get("/user/id").param("id", "1")).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void requestForUserByIdWhenInDatabaseRespondsWithUser() throws Exception {
        Optional<User> result = Optional.of(adam);
        Mockito.when(repository.findById(1L)).thenReturn(result);

        User resultUser = new User();

        String response = mockMvc.perform(get("/user/id").param("id", "1")).andReturn().getResponse().getContentAsString();
        resultUser.setId( Long.parseLong( JsonPath.parse(response).read("$.id").toString() ));
        resultUser.setName(JsonPath.parse(response).read("$.name"));
        resultUser.setEmail(JsonPath.parse(response).read("$.email"));
        resultUser.setPassword(JsonPath.parse(response).read("$.password"));
        resultUser.setElo(JsonPath.parse(response).read("$.elo"));

        Assert.assertEquals(adam, resultUser);
    }

    @Test
    public void requestForUserByIdWhenUserNotInDatabaseRespondsWithNotFound() throws Exception {
        Optional<User> result = Optional.empty();
        Mockito.when(repository.findById(999L)).thenReturn(result);
        mockMvc.perform(get("/user/id").param("id", "999")).andExpect(status().isNotFound());
    }

    @Test
    public void requestForUserByNameResponseTypeIsJsonWithUTF8Charset() throws Exception {
        mockMvc.perform(get("/users/name").param("name", "Eugene")).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void requestForUsersByNameResponseContainsAnArrayOfUsers() throws Exception {
        String response = mockMvc.perform(get("/users/name").param("name", "Eugene")).andReturn().getResponse().getContentAsString();
        Assert.assertNotNull(JsonPath.parse(response).read("$.users.length()"));
    }

    // todo test for name where users are in the mocked dataset - expect the given users (possibly multiple asserts)
    // todo test for name where users are not in the mocked dataset - expect empty list as response


    @Test
    public void requestForUserByInvalidEmailAddressRespondsWithBadRequest() throws Exception {
        mockMvc.perform(get("/user/email").param("email", "info@gmail.commm")).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByValidEmailAddressResponseTypeIsJsonWithUTF8CharsetAndStatusIsOk() throws Exception {
        mockMvc.perform((get("/user/email").param("email", "info@gmail.com"))).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    // todo test for email where user is in the mocked dataset - expect the given user object
    // todo test for email where user is not in the mocked dataset - expect not found response

    @Test
    public void requestForUserByEloResponseTypeIsJsonWithUTF8Charset() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("min_elo", "0");
        params.set("max_elo", "0");
        mockMvc.perform(get("/users/elo").params(params)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void requestForUserByEloMissingParamsRespondsWithBadRequest() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("min_elo", "0");
        mockMvc.perform(get("/users/elo").params(params)).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByEloNotNumberParamsRespondsWithBadRequest() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("min_elo", "0");
        params.set("max_elo", "zero");
        mockMvc.perform(get("/users/elo").params(params)).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByEloNotIntegerParamsRespondsWithBadRequest() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("min_elo", "0");
        params.set("max_elo", "1.1");
        mockMvc.perform(get("/users/elo").params(params)).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByEloTooBigParamsRespondsWithBadRequest() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("min_elo", "0");
        params.set("max_elo", Long.toString(Long.MAX_VALUE));
        mockMvc.perform(get("/users/elo").params(params)).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByEloNegativeParamsRespondsWithOk() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("min_elo", "0");
        params.set("max_elo", "-100");
        mockMvc.perform(get("/users/elo").params(params)).andExpect(status().isOk());
    }

    @Test
    public void requestForUserByEloBetween80And170ResponseIsAdamAndCindy() throws Exception {
        List<User> mockedResultList = new LinkedList<>();
        mockedResultList.add(adam);
        mockedResultList.add(cindy);
        Mockito.when(repository.findAllByEloBetweenOrderByIdAscEloAsc(80, 170)).thenReturn(mockedResultList);
        String[] expected = {"Adam", "Cindy"};

        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("min_elo", "80");
        params.set("max_elo", "170");
        String response = mockMvc.perform(get("/users/elo").params(params)).andReturn().getResponse().getContentAsString();
        JSONArray resultList = JsonPath.parse(response).read("$[*].name");
        String[] results = new String[resultList.size()];
        for (int i = 0; i < resultList.size(); i ++) {
            results[i] = resultList.get(i).toString();
        }
        Assert.assertArrayEquals(expected , results);
    }
}