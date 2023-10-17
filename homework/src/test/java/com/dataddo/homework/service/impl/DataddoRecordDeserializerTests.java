package com.dataddo.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DataddoRecordDeserializerTests {

    @Autowired
    private DataddoRecordDeserializer deserializer;

    @Test
    void testDeserializeLong() {
        // assert that the createByteArrayFromLong is correctly serializing
        // byte array representing 1 in little endian
        byte[] longArray = { 1, 0, 0, 0, 0, 0, 0, 0 };
        byte[] fakeLongArray = { 1, 1, 1, 0, 0, 0, 0, 0 };
        assertEquals(1L, deserializer.desereliazeLong(longArray));
        assertNotEquals(1L, deserializer.desereliazeLong(fakeLongArray));
    }

    @Test
    void testDeserializeString() throws UnsupportedEncodingException {
        byte[] StringValueArray = new byte[64];
        byte[] bytes = "test".getBytes(StandardCharsets.UTF_8);
        System.arraycopy(bytes, 0, StringValueArray, 0, Math.min(bytes.length, 64));

        assertNotEquals("kolo", deserializer.desereliazeString(StringValueArray));
    }

    @Test
    void testDeserializeBool() {
        assertEquals(false, deserializer.desereliazeBoolean((byte) 0));
        assertNotEquals(true, deserializer.desereliazeBoolean((byte) 0));
    }

    @Test
    void testDeserializeTime() {
        byte[] dateArray = { 1, 0, 0, 0, 14, -70, -36, -69, 38, 7, 84, -44, -64, -1, -1, 0 };
        byte[] fakeDateArray = { 1, 0, 0, 0, 14, -70, -15, -80, 25, 7, 84, -44, -64, -1, -1, 0 };
        ZonedDateTime date = ZonedDateTime.of(2005, 10, 10, 19, 42, 30, 123000000, ZoneOffset.UTC);

        assertEquals(date, deserializer.desereliazeDateTime(dateArray));
        assertNotEquals(date, deserializer.desereliazeDateTime(fakeDateArray));
    }

}
