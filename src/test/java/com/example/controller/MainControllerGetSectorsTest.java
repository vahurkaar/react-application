package com.example.controller;

import com.example.model.Sector;
import com.example.repository.SectorRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 20/07/16
 */
public class MainControllerGetSectorsTest extends ControllerTestSupport {

    @Autowired
    @InjectMocks
    private MainController mainController;

    @Mock
    private SectorRepository sectorRepository;

    @Test
    public void testGetSectorsReturnsExpectedResults() throws Exception {
        when(sectorRepository.findByParentId(0L, true, true)).thenReturn(prepareMockSectors());

        mvc.perform(get("/sectors/getAll").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Esimene")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Teine")));
        verify(sectorRepository, times(1)).findByParentId(0L, true, true);
    }

    private List<Sector> prepareMockSectors() {
        List<Sector> sectors = new ArrayList<>();
        sectors.add(new Sector(1L, "Esimene", new Sector(5L, "Alam")));
        sectors.add(new Sector(2L, "Teine"));
        sectors.add(new Sector(3L, "Kolmas"));
        sectors.add(new Sector(4L, "Neljas"));
        return sectors;
    }



}