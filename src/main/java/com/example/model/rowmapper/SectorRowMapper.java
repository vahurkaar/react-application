package com.example.model.rowmapper;

import com.example.model.Sector;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.util.HtmlUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Row mapper class for {@link Sector} typed objects
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 22/07/16
 */
public class SectorRowMapper implements RowMapper<Sector> {

    @Override
    public Sector mapRow(ResultSet rs, int rowNum) throws SQLException {
        Sector sector = new Sector();
        sector.setId(rs.getLong("ID"));
        sector.setName(HtmlUtils.htmlUnescape(rs.getString("NAME")));
        sector.setCreated(rs.getTimestamp("CREATED"));

        return sector;
    }
}
