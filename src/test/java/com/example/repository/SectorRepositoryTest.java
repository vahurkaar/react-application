package com.example.repository;

import com.example.model.Sector;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Vahur Kaar (vahurkaar@gmail.com) on 20/07/16.
 */
@Sql("classpath:sql/schema.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/sectors.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:sql/drop_schema.sql")
public class SectorRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private SectorRepository sectorRepository;

    @Test
    public void testFindById() throws Exception {
        Sector sector = sectorRepository.findById(6L);

        assertEquals(new Long(6), sector.getId());
        assertEquals("Food and Beverage", sector.getName());
        assertEquals(0, sector.getChildren().size());
    }

    @Test
    public void testFindByIdReturnsNothing() throws Exception {
        Sector sector = sectorRepository.findById(9999999L);
        assertNull(sector);
    }

    @Test
    public void testFindAllSectors() throws Exception {
        List<Sector> sectors = sectorRepository.findByParentId(1L);
        assertEquals(10, sectors.size());
        assertEquals(new Long(19), sectors.get(0).getId());
        assertEquals("Construction materials", sectors.get(0).getName());
        assertEquals(new Long(18), sectors.get(1).getId());
        assertEquals("Electronics and Optics", sectors.get(1).getName());
        assertEquals(new Long(6), sectors.get(2).getId());
        assertEquals("Food and Beverage", sectors.get(2).getName());
        assertEquals(7, sectors.get(2).getChildren().size());
    }
}
