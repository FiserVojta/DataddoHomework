package com.dataddo.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dataddo.homework.handler.DataddoFileHandler;
import com.dataddo.homework.model.DataddoRecord;

import java.nio.file.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HomeworkServiceTests {

    @Autowired
    HomeworkService homeworkService;

    @Autowired
    DataddoFileHandler dataddoFileHandler;

    @BeforeEach
    public void loadOneRecordToFile() throws Exception {
        Files.deleteIfExists(Paths.get(dataddoFileHandler.getFileName()));
        homeworkService.createReDataddoRecord(createDummyRecord());
    }

    @Test
    void testValidateDataddoRecord() {
        DataddoRecord record = new DataddoRecord();
        record.setStrValue("asdasdasd");
        assertTrue(homeworkService.validateDataddoRecord(record));
    }

    @Test
    void testCreateReDataddoRecord() throws Exception {
        DataddoRecord record = createDummyRecord();
        homeworkService.createReDataddoRecord(record);

        assertEquals(2, record.getID());
        assertEquals(196, dataddoFileHandler.getFileLength());
    }

    @Test
    void testUpdateReDataddoRecord() throws Exception {
        ZonedDateTime date = ZonedDateTime.of(2010, 10, 10, 19, 42, 30, 123000000, ZoneOffset.UTC);

        DataddoRecord record = createDummyRecord();
        record.setBoolValue(false);
        record.setIntValue(123L);
        record.setStrValue("kolotoc");
        record.setTimeValue(date);
        homeworkService.updateRecord(record, 1L);

        // fetch the newly updated record
        DataddoRecord fetchedRecord = homeworkService.getRecordById(1L);

        assertEquals(1L, fetchedRecord.getID());
        assertEquals(123L, fetchedRecord.getIntValue());
        assertEquals("kolotoc", fetchedRecord.getStrValue());
        assertEquals(false, fetchedRecord.getBoolValue());
        assertEquals(date, fetchedRecord.getTimeValue());
    }

    @Test
    void testDeleteDataddoRecord() throws Exception {
        homeworkService.deleteRecord(1L);

        DataddoRecord fetchedRecord = homeworkService.getRecordById(1L);
        assertEquals(0, fetchedRecord.getID());

    }

    private DataddoRecord createDummyRecord() {
        DataddoRecord record = new DataddoRecord();
        record.setIntValue(156L);
        record.setStrValue("test");
        record.setBoolValue(true);
        record.setTimeValue(ZonedDateTime.of(2005, 10, 10, 19, 42, 30, 123000000, ZoneOffset.UTC));
        return record;
    }

}
