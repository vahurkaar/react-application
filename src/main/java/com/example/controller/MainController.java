package com.example.controller;

import com.example.converter.UserDataFormConverter;
import com.example.dto.UserDataForm;
import com.example.dto.UserSession;
import com.example.exception.ValidationException;
import com.example.model.Sector;
import com.example.model.UserData;
import com.example.repository.SectorRepository;
import com.example.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Main controller, which provides the following services:
 * <ul>
 *  <li>Fetch all sectors</li>
 *  <li>Fetch user data from session</li>
 *  <li>Save user data (to the database and to the session</li>
 * </ul>
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 20/07/16
 */
@RestController
public class MainController {

    @Autowired
    private UserSession userSession;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private UserDataFormConverter userDataFormConverter;


    @RequestMapping(value = "/sectors/getAll", method = RequestMethod.GET)
    public List<Sector> getSectors() {
        return sectorRepository.findByParentId(0L, true, true);
    }

    @RequestMapping(value = "/userData/get", method = RequestMethod.GET)
    public UserData get() {
        return userSession.getUserData();
    }

    @RequestMapping(value = "/userData/save", method = RequestMethod.POST)
    public String saveUserData(@Valid @RequestBody UserDataForm userDataForm, Errors errors)
            throws ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        UserData userData = prepareUserData(userDataForm);
        UserData savedUserData = userDataRepository.save(userData);
        userSession.setUserData(savedUserData);
        return "OK";
    }

    private UserData prepareUserData(UserDataForm userDataForm) {
        UserData userData = userDataFormConverter.convert(userDataForm);
        userData.setId(userSession.getUserData() != null ? userSession.getUserData().getId() : null);
        return userData;
    }

}
