package com.codecool.matchmakingservice.userservice.controller;

import com.codecool.matchmakingservice.userservice.model.UserStatus;
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
    private User wendy;

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
    public void wrongURLRespondsWithNotFound() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isNotFound());
    }

    @Test
    public void requestForUserWithNoParamsRespondsWithListOfUsers() throws Exception {
        String response = mockMvc.perform(get("/user")).andReturn().getResponse().getContentAsString();
        Assert.assertNotNull(JsonPath.parse(response).read("$.length()"));
    }

    @Test
    public void requestForUserByNotNumericIdRespondsBadRequest() throws Exception {
        mockMvc.perform(get("/user/twentytwo")).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByNegativeIdRespondsBadRequest() throws Exception {
        mockMvc.perform(get("/user/-15")).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByZeroIdRespondsBadRequest() throws Exception {
        mockMvc.perform(get("/user/0")).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByIdResponseTypeIsJsonWithUTF8Charset() throws Exception {
        mockMvc.perform(get("/user/1")).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void requestForUserByIdWhenInDatabaseRespondsWithUser() throws Exception {
        Optional<User> result = Optional.of(adam);
        Mockito.when(repository.findById(1L)).thenReturn(result);

        User resultUser = new User();

        String response = mockMvc.perform(get("/user/1")).andReturn().getResponse().getContentAsString();
        resultUser.setId(Long.parseLong(JsonPath.parse(response).read("$.id").toString()));
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
        mockMvc.perform(get("/user/999")).andExpect(status().isNotFound());
    }

    @Test
    public void requestForUserByNameResponseTypeIsJsonWithUTF8Charset() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("name", "Eugene");
        mockMvc.perform(get("/user").params(params)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void requestForUsersByNameResponseContainsAnArrayOfUsers() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("name", "Eugene");
        String response = mockMvc.perform(get("/user").params(params)).andReturn().getResponse().getContentAsString();
        Assert.assertNotNull(JsonPath.parse(response).read("$.length()"));
    }

    @Test
    public void requestForUsersByNameWhenNotInDatabaseResponseIsEmptyList() throws Exception {
        List<User> mockedResultList = new LinkedList<>();
        Mockito.when(repository.findAllByNameContainingIgnoreCaseOrderByIdAscNameAsc("Eugene")).thenReturn(mockedResultList);
        String[] expected = {};

        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("name", "Eugene");
        String response = mockMvc.perform(get("/user").params(params)).andReturn().getResponse().getContentAsString();
        JSONArray resultList = JsonPath.parse(response).read("$[*].name");
        String[] results = new String[resultList.size()];
        for (int i = 0; i < resultList.size(); i++) {
            results[i] = resultList.get(i).toString();
        }
        Assert.assertArrayEquals(expected, results);
    }

    @Test
    public void requestForUsersByNameReturnsListOfUsersWithMatchingNames() throws Exception {
        List<User> mockedResultList = new LinkedList<>();
        mockedResultList.add(wendy);
        mockedResultList.add(cindy);
        Mockito.when(repository.findAllByNameContainingIgnoreCaseOrderByIdAscNameAsc("dy")).thenReturn(mockedResultList);

        String[] expected = {"Wendy", "Cindy"};

        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("name", "dy");
        String response = mockMvc.perform(get("/user").params(params)).andReturn().getResponse().getContentAsString();
        JSONArray resultList = JsonPath.parse(response).read("$[*].name");
        String[] results = new String[resultList.size()];
        for (int i = 0; i < resultList.size(); i++) {
            results[i] = resultList.get(i).toString();
        }
        Assert.assertArrayEquals(expected, results);
    }

    //todo get a reliabe list of fake emails and use rhose for invalid email testing
    @Test
    public void requestForUserByInvalidEmailAddressRespondsWithBadRequest() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("email", "info@gmail.commm");
        mockMvc.perform(get("/user").params(params)).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByFormallyValidEmailAddressResponseTypeIsJsonWithUTF8Charset() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("email", "info@gmail.com");
        mockMvc.perform((get("/user").params(params))).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void requestForUserByEmailWhenNotInDatabaseRespondsNotFound() throws Exception {
        Optional<User> result = Optional.empty();
        Mockito.when(repository.findByEmail("adam@mms.com")).thenReturn(result);

        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("email", "adam@mms.com");
        mockMvc.perform(get("/user").params(params)).andExpect(status().isNotFound());
    }

    @Test
    public void requestForUserByEmailWhenInDatabaseResponseIsOneElementArray() throws Exception {
        Optional<User> result = Optional.of(adam);
        Mockito.when(repository.findByEmail("adam@mms.com")).thenReturn(result);


        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("email", "adam@mms.com");
        String response = mockMvc.perform(get("/user").params(params)).andReturn().getResponse().getContentAsString();
        int resultLength = Integer.parseInt(JsonPath.parse(response).read("$.length()").toString());

        Assert.assertEquals(1, resultLength);
    }

    @Test
    public void requestForUserByEmailWhenInDatabaseRespondsWithUser() throws Exception {
        Optional<User> result = Optional.of(adam);
        Mockito.when(repository.findByEmail("adam@mms.com")).thenReturn(result);

        User resultUser = new User();

        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("email", "adam@mms.com");
        String response = mockMvc.perform(get("/user").params(params)).andReturn().getResponse().getContentAsString();
        resultUser.setId(Long.parseLong(JsonPath.parse(response).read("$[0].id").toString()));
        resultUser.setName(JsonPath.parse(response).read("$[0].name"));
        resultUser.setEmail(JsonPath.parse(response).read("$[0].email"));
        resultUser.setPassword(JsonPath.parse(response).read("$[0].password"));
        resultUser.setElo(JsonPath.parse(response).read("$[0].elo"));

        Assert.assertEquals(adam, resultUser);
    }

    @Test
    public void requestForUserByEloResponseTypeIsJsonWithUTF8Charset() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("min_elo", "0");
        params.set("max_elo", "0");
        mockMvc.perform(get("/user").params(params)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void requestForUserByEloMissingParamsRespondsWithBadRequest() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("min_elo", "0");
        mockMvc.perform(get("/user").params(params)).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByEloNotNumberParamsRespondsWithBadRequest() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("min_elo", "0");
        params.set("max_elo", "zero");
        mockMvc.perform(get("/user").params(params)).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByEloNotIntegerParamsRespondsWithBadRequest() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("min_elo", "0");
        params.set("max_elo", "1.1");
        mockMvc.perform(get("/user").params(params)).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByEloTooBigParamsRespondsWithBadRequest() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("min_elo", "0");
        params.set("max_elo", Long.toString(Long.MAX_VALUE));
        mockMvc.perform(get("/user").params(params)).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUserByEloNegativeParamsRespondsWithOk() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("min_elo", "0");
        params.set("max_elo", "-100");
        mockMvc.perform(get("/user").params(params)).andExpect(status().isOk());
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
        String response = mockMvc.perform(get("/user").params(params)).andReturn().getResponse().getContentAsString();
        JSONArray resultList = JsonPath.parse(response).read("$[*].name");
        String[] results = new String[resultList.size()];
        for (int i = 0; i < resultList.size(); i++) {
            results[i] = resultList.get(i).toString();
        }
        Assert.assertArrayEquals(expected, results);
    }

    @Test
    public void requestForUserByStatusResponseTypeIsJsonWithUTF8Charset() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("status", UserStatus.ONLINE.toString());
        mockMvc.perform(get("/user").params(params)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void requestForUsersByStatusResponseContainsAnArrayOfUsers() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("status", UserStatus.ONLINE.toString());
        String response = mockMvc.perform(get("/user").params(params)).andReturn().getResponse().getContentAsString();
        Assert.assertNotNull(JsonPath.parse(response).read("$.length()"));
    }

    @Test
    public void requestForUsersByInvalidStatusRespondsWithBadRequest() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("status", "trolololol");
        mockMvc.perform(get("/user").params(params)).andExpect(status().isBadRequest());
    }

    @Test
    public void requestForUsersByStatusWhenNoUserByThatStatusIsEmptyList() throws Exception {
        List<User> mockedResultList = new LinkedList<>();
        Mockito.when(repository.findAllByStatusOrderByIdAscNameAsc(UserStatus.IN_GAME)).thenReturn(mockedResultList);
        String[] expected = {};

        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("status", UserStatus.IN_GAME.toString() );
        String response = mockMvc.perform(get("/user").params(params)).andReturn().getResponse().getContentAsString();
        JSONArray resultList = JsonPath.parse(response).read("$[*].name");
        String[] results = new String[resultList.size()];
        for (int i = 0; i < resultList.size(); i++) {
            results[i] = resultList.get(i).toString();
        }
        Assert.assertArrayEquals(expected, results);
    }

    @Test
    public void requestForUsersByStatusReturnsListOfUsersWithMatchingNames() throws Exception {
        List<User> mockedResultList = new LinkedList<>();
        mockedResultList.add(adam);
        mockedResultList.add(wendy);
        Mockito.when(repository.findAllByStatusOrderByIdAscNameAsc(UserStatus.OFFLINE)).thenReturn(mockedResultList);

        String[] expected = {"Adam", "Wendy"};

        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("status", UserStatus.OFFLINE.toString());
        String response = mockMvc.perform(get("/user").params(params)).andReturn().getResponse().getContentAsString();
        JSONArray resultList = JsonPath.parse(response).read("$[*].name");
        String[] results = new String[resultList.size()];
        for (int i = 0; i < resultList.size(); i++) {
            results[i] = resultList.get(i).toString();
        }
        Assert.assertArrayEquals(expected, results);
    }

}