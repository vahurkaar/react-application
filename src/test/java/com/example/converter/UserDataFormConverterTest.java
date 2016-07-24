package com.example.converter;

import com.example.dto.UserDataForm;
import com.example.model.Sector;
import com.example.model.UserData;
import com.example.repository.SectorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 22/07/16
 */
public class UserDataFormConverterTest {

    @InjectMocks
    private UserDataFormConverter converter = new UserDataFormConverter();

    @Mock
    private SectorRepository sectorRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConvert() throws Exception {
        UserDataForm form = new UserDataForm();
        form.setAgreeTerms(true);
        form.setUserFullName("Nimi");
        form.setUserSectors(new Long[] {1L, 2L});

        when(sectorRepository.findById(1L)).thenReturn(new Sector(1L, "Sector 1"));
        when(sectorRepository.findById(2L)).thenReturn(new Sector(2L, "Sector 1"));
        UserData result = converter.convert(form);

        assertNull(result.getId());
        assertEquals(form.getUserFullName(), result.getName());
        assertEquals(2, result.getUserSectors().size());
        verify(sectorRepository, times(1)).findById(eq(1L));
        verify(sectorRepository, times(1)).findById(eq(2L));
    }
}