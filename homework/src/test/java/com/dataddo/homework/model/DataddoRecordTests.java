package com.dataddo.homework.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DataddoRecordTests {

    // test creation of DataddoRecord
    @Test
    void testDatarecord() {
        ZonedDateTime date = ZonedDateTime.of(2005, 10, 10, 19, 42, 30, 123000000, ZoneOffset.UTC);
        ZonedDateTime fakeDate = ZonedDateTime.of(2007, 10, 10, 15, 42, 30, 123000000, ZoneOffset.UTC);

        // Create a DataddoRecord instance and serialize it
        DataddoRecord record = new DataddoRecord();
        record.setID(1L);
        record.setIntValue(100L);
        record.setStrValue("test");
        record.setBoolValue(true);
        record.setTimeValue(date);

        assertEquals(1L, record.getID());
        assertNotEquals(15L, record.getID());

        assertEquals(100L, record.getIntValue());
        assertNotEquals(15L, record.getIntValue());

        assertEquals("test", record.getStrValue());
        assertNotEquals("kolo", record.getStrValue());

        assertEquals(true, record.getBoolValue());
        assertNotEquals(false, record.getBoolValue());

        assertEquals(date, record.getTimeValue());
        assertNotEquals(fakeDate, record.getTimeValue());

    }

}
