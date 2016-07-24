package com.example.repository.impl;

import com.example.model.Sector;
import com.example.model.rowmapper.SectorRowMapper;
import com.example.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * {@link SectorRepository} default implementation, which uses a {@link JdbcTemplate}.
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 20/07/16
 */
@Repository
public class SectorRepositoryImpl implements SectorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public Sector findById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM SECTOR WHERE ID = ?", new SectorRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sector> findByParentId(Long parentId) {
        return findByParentId(parentId, true, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sector> findByParentId(Long parentId, boolean fetchChildren, boolean deepFetch) {
        List<Sector> sectors = jdbcTemplate.query("SELECT * FROM SECTOR WHERE PARENT = ? ORDER BY NAME",
                new Object[]{parentId}, new SectorRowMapper());

        if (fetchChildren) {
            for (Sector sector : sectors) {
                sector.getChildren().addAll(findByParentId(sector.getId(), deepFetch, deepFetch));
            }
        }

        return sectors;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sector> findUserSectors(Long userId) {
        return jdbcTemplate.query("SELECT * FROM SECTOR S JOIN USER_SECTOR US ON US.SECTOR_ID = S.ID WHERE US.USER_ID = ? ORDER BY S.ID",
                new Object[]{userId}, new SectorRowMapper());
    }
}
