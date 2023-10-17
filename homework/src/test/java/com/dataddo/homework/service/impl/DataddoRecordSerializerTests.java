package com.dataddo.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dataddo.homework.model.DataddoRecord;

import java.nio.charset.StandardCharsets;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataddoRecordSerializerTests {

    @Autowired
    private DataddoRecordSerializer serializer;

    @Test
    void testSerializeDataddoRecord() throws Exception {

        // Create a DataddoRecord instance and serialize it
        DataddoRecord record = new DataddoRecord();
        record.setID(1L);
        record.setIntValue(100L);
        record.setStrValue("test");
        record.setBoolValue(true);
        record.setTimeValue(ZonedDateTime.now());

        byte[] result = serializer.serializeDataddoRecord(record);

        // Assert the length of the result
        assertEquals(98, result.length);

    }

    @Test
    void testSerializeInt() {
        // assert that the createByteArrayFromLong is correctly serializing
        // byte array representing 1 in little endian
        byte[] IDarray = { 1, 0, 0, 0, 0, 0, 0, 0 };
        assertArrayEquals(IDarray, serializer.createByteArrayFromLong(1L));
    }

    @Test
    void testSerializeString() {
        // assert string value
        byte[] StringValueArray = new byte[64];
        byte[] bytes = "test".getBytes(StandardCharsets.UTF_8);
        System.arraycopy(bytes, 0, StringValueArray, 0, Math.min(bytes.length, 64));
        assertArrayEquals(StringValueArray, serializer.createByteArrayFromString("test"));

    }

    @Test
    void testSerializeBool() {
        // assert boolean
        byte[] boolByte = { 1 };
        assertArrayEquals(boolByte, serializer.createByteArrayFromBoolean(true));
    }

    @Test
    void testSerializeDate() throws Exception {
        // assert date serialization
        ZonedDateTime date = ZonedDateTime.of(2005, 10, 10, 19, 42, 30, 123000000, ZoneOffset.UTC);
        byte[] dateArray = { 1, 0, 0, 0, 14, -70, -36, -69, 38, 7, 84, -44, -64, -1, -1, 0 };
        assertArrayEquals(dateArray, serializer.createByteArrayFromDateTime(date));

    }

}