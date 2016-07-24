package com.example.controller;

import com.example.dto.UserSession;
import com.example.model.Sector;
import com.example.model.UserData;
import com.example.repository.UserDataRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 23/07/16
 */
public class MainControllerGetUserDataTest extends ControllerTestSupport {

    @InjectMocks
    @Autowired
    private MainController mainController;

    @Autowired
    private MockHttpSession session;

    @Mock
    private UserDataRepository userDataRepository;

    @Test
    public void testGetUserDataEmptyResult() throws Exception {
        MvcResult result = mvc.perform(get("/userData/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(status().isOk()).andReturn();

        assertEquals("", result.getResponse().getContentAsString());
    }

    @Test
    public void testGetUserDataSessionObject() throws Exception {
        UserSession userSession = new UserSession();

        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName("Nimi");
        userData.getUserSectors().addAll(Arrays.asList(new Sector(1L, "Esimene"), new Sector(2L, "Teine")));

        userSession.setUserData(userData);
        session.setAttribute("scopedTarget.userSession", userSession);

        mvc.perform(get("/userData/get")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Nimi")))
                .andExpect(jsonPath("$.userSectors[0].id", is(1)))
                .andExpect(jsonPath("$.userSectors[0].name", is("Esimene")))
                .andExpect(jsonPath("$.userSectors[1].id", is(2)))
                .andExpect(jsonPath("$.userSectors[1].name", is("Teine")));
    }
}