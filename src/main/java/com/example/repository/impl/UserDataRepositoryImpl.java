package com.example.repository.impl;

import com.example.model.Sector;
import com.example.model.UserData;
import com.example.model.rowmapper.UserDataRowMapper;
import com.example.repository.SectorRepository;
import com.example.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * Created by Vahur Kaar (vahurkaar@gmail.com) on 20/07/16.
 */
@Repository
public class UserDataRepositoryImpl implements UserDataRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SectorRepository sectorRepository;

    @Override
    @Transactional(readOnly = true)
    public UserData findById(Long id) {
        try {
            UserData userData = jdbcTemplate.queryForObject("SELECT * FROM USER_DATA WHERE ID = ?",
                    new UserDataRowMapper(), id);
            userData.getUserSectors().addAll(sectorRepository.findUserSectors(id));
            return userData;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public UserData save(UserData userData) {
        if (userDataExists(userData)) {
            updateUserData(userData);

            removeUserSectors(userData);
            for (Sector userSector : userData.getUserSectors()) {
                saveUserSector(userData, userSector);
            }
        } else {
            saveUserData(userData);

            for (Sector userSector : userData.getUserSectors()) {
                saveUserSector(userData, userSector);
            }
        }

        return findById(userData.getId());
    }

    private boolean userDataExists(UserData userData) {
        Integer userExists = jdbcTemplate.queryForObject("SELECT COUNT(ID) FROM USER_DATA WHERE ID = ?",
                Integer.class, userData.getId());
        return userExists > 0;
    }

    private void removeUserSectors(UserData userData) {
        jdbcTemplate.update("DELETE FROM USER_SECTOR WHERE USER_ID = ?", userData.getId());
    }

    private void updateUserData(UserData userData) {
        jdbcTemplate.update("UPDATE USER_DATA SET NAME = ?, MODIFIED = ? WHERE ID = ?",
                userData.getName(), new Timestamp(new Date(System.currentTimeMillis()).getTime()),
                userData.getId());
    }

    private void saveUserData(UserData userData) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO USER_DATA (NAME) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, userData.getName());
            return ps;
        }, keyHolder);
        userData.setId(Long.valueOf(keyHolder.getKeys().get("id").toString()));
    }

    private void saveUserSector(UserData userData, Sector userSector) {
        jdbcTemplate.update("INSERT INTO USER_SECTOR (USER_ID, SECTOR_ID) VALUES (?, ?)",
                userData.getId(), userSector.getId());
    }
}
