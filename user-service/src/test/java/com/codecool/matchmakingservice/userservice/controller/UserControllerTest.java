package com.codecool.matchmakingservice.userservice.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void wrongURLRespondsWithNotFound() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isNotFound());
    }

    @Test
    public void requestForUserByIdResponseTypeIsJsonWithUTF8Charset() throws Exception {
        mockMvc.perform(get("/user/id").param("id", "1")).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void requestForUserByIdResponseContainsId() throws Exception {
        String response = mockMvc.perform(get("/user/id").param("id", "1")).andReturn().getResponse().getContentAsString();
        Assert.assertTrue(JsonPath.parse(response).read("$.id").equals(1));
    }

    // todo test for id where user is in the mocked dataset - expect the given user object
    // todo test for id where user is not in the mocked dataset - expect not found response

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

    // todo controller for getting users based on elo rating (range)

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
}