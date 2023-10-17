package com.dataddo.homework.handler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dataddo.homework.model.DataddoRecord;
import com.dataddo.homework.service.impl.DataddoRecordSerializer;

import java.io.IOException;
import java.nio.file.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DataddoFileHandlerTests {

    private final int RECORDLENGHT = 98;

    @BeforeEach
    public void deleteFileBeforeTest() throws IOException {
        Files.deleteIfExists(Paths.get(dataddoFileHandler.getFileName()));
    }

    @AfterEach
    public void deleteFileAfterTest() throws IOException {
        Files.deleteIfExists(Paths.get(dataddoFileHandler.getFileName()));
    }

    @Autowired
    DataddoFileHandler dataddoFileHandler;

    @Autowired
    DataddoRecordSerializer dataddoRecordSerializer;

    @Test
    void testFileName() {
        assertEquals("records.bin", dataddoFileHandler.getFileName());
        assertNotEquals("fake.bin", dataddoFileHandler.getFileName());
    }

    @Test
    void testFileCreation() throws Exception {
        byte[] IDarray = { 1, 0, 0, 0, 0, 0, 0, 0 };
        assertFalse(Files.exists(Paths.get(dataddoFileHandler.getFileName())));
        dataddoFileHandler.writeRecordToFile(1, IDarray);
        assertTrue(Files.exists(Paths.get(dataddoFileHandler.getFileName())), "File should be created");
    }

    @Test
    void testFileLenght() throws Exception {
        byte[] IDarray = { 1, 0, 0, 0, 0, 0, 0, 0 };
        dataddoFileHandler.writeRecordToFile(0, IDarray);
        assertEquals(8, dataddoFileHandler.getFileLength());
        assertNotEquals(15, dataddoFileHandler.getFileLength());
    }

    // test if the program can read correctly saved byte array in file
    @Test
    void testFileRead() throws Exception {
        // write a record to filesystem
        DataddoRecord record = new DataddoRecord();
        record.setID(1L);
        record.setIntValue(156L);
        record.setStrValue("test");
        record.setBoolValue(true);
        record.setTimeValue(ZonedDateTime.of(2005, 10, 10, 19, 42, 30, 123000000, ZoneOffset.UTC));
        byte[] byteArray = dataddoRecordSerializer.serializeDataddoRecord(record);
        dataddoFileHandler.writeRecordToFile(0, byteArray);

        assertArrayEquals(byteArray, dataddoFileHandler.readRecordFromFile(0, RECORDLENGHT));
        assertEquals(98, dataddoFileHandler.getFileLength());
    }

}
