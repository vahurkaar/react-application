package com.example.util;

import org.junit.After;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 22/07/16
 */
public class SectorsHtmlToSqlTest {

    public static final String TEST_RESULT_PATH = "src/test/resources/sectors.sql";
    public static final String TEST_SOURCE_PATH = "src/test/resources/sectors.xml";
    public static final String TEST_ACTUAL_FILE_PATH = "src/test/resources/sql/sectors.sql";

    private SectorsHtmlToSql converter = new SectorsHtmlToSql();

    @Test
    public void testConvert() throws Exception {
        converter.convert(TEST_SOURCE_PATH, TEST_RESULT_PATH);
        assertEquals(Files.readAllLines(Paths.get(TEST_RESULT_PATH)),
                Files.readAllLines(Paths.get(TEST_ACTUAL_FILE_PATH)));
    }

    @After
    public void tearDown() throws Exception {
        Files.delete(Paths.get(TEST_RESULT_PATH));
    }
}