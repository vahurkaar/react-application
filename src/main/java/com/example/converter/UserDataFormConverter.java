package com.example.converter;

import com.example.dto.UserDataForm;
import com.example.model.Sector;
import com.example.model.UserData;
import com.example.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Converts the given {@link UserDataForm} object to an instance of {@link UserData}.
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 22/07/16
 */
@Component
public class UserDataFormConverter implements Converter<UserDataForm, UserData> {

    @Autowired
    private SectorRepository sectorRepository;

    @Override
    public UserData convert(UserDataForm userDataForm) {
        UserData userData = new UserData();
        userData.setName(userDataForm.getUserFullName());
        userData.getUserSectors().addAll(
                convertUserSectors(userDataForm.getUserSectors()));

        return userData;
    }

    private Collection<Sector> convertUserSectors(Long[] userSectors) {
        List<Sector> sectors = new ArrayList<>();
        for (Long sector : userSectors) {
            sectors.add(sectorRepository.findById(sector));
        }

        return sectors;
    }
}
