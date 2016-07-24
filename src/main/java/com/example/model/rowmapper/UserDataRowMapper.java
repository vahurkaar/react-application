package com.example.model.rowmapper;

import com.example.model.UserData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Row mapper class for {@link UserData} typed objects
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 22/07/16
 */
public class UserDataRowMapper implements RowMapper<UserData> {

    @Override
    public UserData mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserData userData = new UserData();
        userData.setId(rs.getLong("ID"));
        userData.setName(rs.getString("NAME"));
        userData.setCreated(rs.getTimestamp("CREATED"));
        userData.setModified(rs.getTimestamp("MODIFIED"));

        return userData;
    }
}
