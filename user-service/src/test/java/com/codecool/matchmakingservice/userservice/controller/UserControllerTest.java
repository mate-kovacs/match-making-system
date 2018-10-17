package com.codecool.matchmakingservice.userservice.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void wrongURLRespondsWithNotFound() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().is4xxClientError());
    }

    @Test
    public void requestForUserByIdResponseTypeIsJsonWithUTF8Charset() throws Exception {
        mockMvc.perform(get("/user/1")).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

}