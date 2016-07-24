package com.example.repository;

import com.example.model.Sector;
import com.example.model.UserData;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;
import static org.mockito.Matchers.any;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 20/07/16
 */
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
        "classpath:sql/schema.sql",
        "classpath:sql/sectors.sql",
        "classpath:sql/user_data.sql"
})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts =
        "classpath:sql/drop_schema.sql"
)
public class UserDataRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    @Test
    public void testSaveUserDataInsert() throws Exception {
        UserData userData = new UserData();
        userData.setName("Kasutaja");

        Set<Sector> userSectors = new HashSet<>();
        userSectors.addAll(sectorRepository.findByParentId(0L));
        userData.getUserSectors().addAll(userSectors);

        userDataRepository.save(userData);

        UserData savedUserData = userDataRepository.findById(2L);
        assertEquals(new Long(2), savedUserData.getId());
        assertEquals("Kasutaja", savedUserData.getName());
        assertEquals(3, savedUserData.getUserSectors().size());
    }

    @Test(expected = Exception.class)
    public void testSaveUserDataTooLongName() throws Exception {
        UserData userData = new UserData();
        userData.setName("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

        userDataRepository.save(userData);
    }

    @Test
    public void testSaveUserDataUpdate() throws Exception {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setName("Kasutaja");

        Set<Sector> userSectors = new HashSet<>();
        userSectors.addAll(sectorRepository.findByParentId(0L));
        userData.getUserSectors().addAll(userSectors);

        userDataRepository.save(userData);

        UserData savedUserData = userDataRepository.findById(1L);
        assertEquals(new Long(1), savedUserData.getId());
        assertEquals("Kasutaja", savedUserData.getName());
        assertEquals(3, savedUserData.getUserSectors().size());
        assertNull(userDataRepository.findById(2L));
    }

    @Test
    public void testSaveUserDataIdDoesNotExist() throws Exception {
        UserData userData = new UserData();
        userData.setId(5L);
        userData.setName("Kasutaja");

        Set<Sector> userSectors = new HashSet<>();
        userSectors.addAll(sectorRepository.findByParentId(0L));
        userData.getUserSectors().addAll(userSectors);

        userDataRepository.save(userData);

        UserData savedUserData = userDataRepository.findById(2L);
        assertEquals(new Long(2), savedUserData.getId());
        assertEquals("Kasutaja", savedUserData.getName());
        assertEquals(3, savedUserData.getUserSectors().size());
    }

    @Test
    public void testFindUserData() throws Exception {
        UserData userdata = userDataRepository.findById(1L);

        assertEquals(new Long(1), userdata.getId());
        assertEquals("Nimi", userdata.getName());
        assertEquals(2, userdata.getUserSectors().size());
        assertEquals(new Long(42), userdata.getUserSectors().get(0).getId());
        assertEquals(new Long(43), userdata.getUserSectors().get(1).getId());
    }
}
