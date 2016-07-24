package com.example.controller;

import com.example.converter.UserDataFormConverter;
import com.example.dto.UserDataForm;
import com.example.dto.UserSession;
import com.example.model.UserData;
import com.example.repository.SectorRepository;
import com.example.repository.UserDataRepository;
import com.example.util.TestUtil;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 22/07/16
 */
public class MainControllerSaveUserDataTest extends ControllerTestSupport {

    @Autowired
    @InjectMocks
    private MainController mainController;

    @Autowired
    private MockHttpSession session;

    @Mock
    private UserDataRepository userDataRepository;

    @Mock
    private SectorRepository sectorRepository;

    @Mock
    private UserDataFormConverter userDataFormConverter;

    @Test
    public void testRequiredFieldsEn() throws Exception {
        mvc.perform(post("/userData/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("agreeTerms")))
                .andExpect(jsonPath("$[0].errorMessage", is("You are required to accept the terms!")))
                .andExpect(jsonPath("$[1].field", is("userFullName")))
                .andExpect(jsonPath("$[1].errorMessage", is("Name is required!")))
                .andExpect(jsonPath("$[2].field", is("userSectors")))
                .andExpect(jsonPath("$[2].errorMessage", is("Select at least one sector!")));
        verify(userDataRepository, never()).save(any(UserData.class));
    }

    @Test
    public void testRequiredFieldsEt() throws Exception {
        mvc.perform(post("/userData/save?lang=et")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("agreeTerms")))
                .andExpect(jsonPath("$[0].errorMessage", is("Te peate nõustuma tingimustega, et jätkata!")))
                .andExpect(jsonPath("$[1].field", is("userFullName")))
                .andExpect(jsonPath("$[1].errorMessage", is("Nimi on kohustuslik!")))
                .andExpect(jsonPath("$[2].field", is("userSectors")))
                .andExpect(jsonPath("$[2].errorMessage", is("Valige vähemalt üks valdkond!")));
        verify(userDataRepository, never()).save(any(UserData.class));
    }

    @Test
    public void testTooLongName() throws Exception {
        UserDataForm form = new UserDataForm();
        form.setUserFullName("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        form.setAgreeTerms(true);
        form.setUserSectors(new Long[] {1L, 2L});

        mvc.perform(post("/userData/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(form))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("userFullName")))
                .andExpect(jsonPath("$[0].errorMessage", is("Name can be 255 characters long!")));
    }

    @Test
    public void testSuccessfulSaving() throws Exception {
        UserDataForm form = new UserDataForm();
        form.setUserFullName("Nimi");
        form.setAgreeTerms(true);
        form.setUserSectors(new Long[] {1L, 2L});

        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(form.getUserFullName());

        when(userDataFormConverter.convert(any())).thenReturn(userData);

        MvcResult result = mvc.perform(post("/userData/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(form))
        )
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("OK", result.getResponse().getContentAsString());
        verify(userDataRepository, times(1)).save(any(UserData.class));
    }

    @Test
    public void testSavingThrowsException() throws Exception {
        UserDataForm form = new UserDataForm();
        form.setUserFullName("Nimi");
        form.setAgreeTerms(true);
        form.setUserSectors(new Long[] {1L, 2L});

        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(form.getUserFullName());

        when(userDataFormConverter.convert(any())).thenReturn(userData);
        when(userDataRepository.save(any(UserData.class))).thenThrow(new QueryTimeoutException("Error message!"));

        MvcResult result = mvc.perform(post("/userData/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(form))
        )
                .andExpect(status().isInternalServerError())
                .andReturn();

        assertEquals("Error message!", result.getResponse().getContentAsString());
        verify(userDataRepository, times(1)).save(any(UserData.class));
    }

    @Test
    public void testUserDataIsSavedInSession() throws Exception {
        UserDataForm form = new UserDataForm();
        form.setUserFullName("Nimi");
        form.setAgreeTerms(true);
        form.setUserSectors(new Long[] {1L, 2L});

        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName(form.getUserFullName());

        when(userDataFormConverter.convert(any())).thenReturn(userData);
        when(userDataRepository.save(any())).thenReturn(new UserData());

        mvc.perform(post("/userData/save")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(form))
        ).andExpect(status().isOk());

        assertNotNull(((UserSession) session.getAttribute("scopedTarget.userSession")).getUserData());
    }

    @Test
    public void testUserDataIsUpdatedInSession() throws Exception {
        UserDataForm form = new UserDataForm();
        form.setUserFullName("Nimi");
        form.setAgreeTerms(true);
        form.setUserSectors(new Long[] {1L, 2L});

        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName("Vana nimi");

        UserSession userSession = new UserSession();
        userSession.setUserData(userData);
        session.setAttribute("scopedTarget.userSession", userSession);

        UserData savedUserData = new UserData();
        savedUserData.setId(1L);
        savedUserData.setName("Nimi");

        when(userDataFormConverter.convert(any())).thenReturn(savedUserData);
        when(userDataRepository.save(any())).thenReturn(savedUserData);

        mvc.perform(post("/userData/save")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(form))
        ).andExpect(status().isOk());

        assertEquals("Nimi", ((UserSession) session.getAttribute("scopedTarget.userSession")).getUserData().getName());
    }
}