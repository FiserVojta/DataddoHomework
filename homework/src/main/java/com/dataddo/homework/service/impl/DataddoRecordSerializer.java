package com.dataddo.homework.service.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;

import com.dataddo.homework.model.DataddoRecord;

@Component
public class DataddoRecordSerializer {

    private int totalLength = 98;

    // difference from standart epoch to GO epoch
    private final Long SECONDS_PER_DAY = 86400L;
    private final Long GO_MAGIC_UNIX_TO_INTERNAL = (1969L * 365 + 1969 / 4 - 1969 / 100 + 1969 / 400) * SECONDS_PER_DAY;

    // create byte array of set lenght 98 from dataddoRecord object
    public byte[] serializeDataddoRecord(DataddoRecord object) throws Exception {
        byte[] result = new byte[totalLength];
        System.arraycopy(createByteArrayFromLong(object.getID()), 0, result, 0, 8);
        System.arraycopy(createByteArrayFromLong(object.getIntValue()), 0, result, 8, 8);
        System.arraycopy(createByteArrayFromString(object.getStrValue()), 0, result, 16, 64);
        System.arraycopy(createByteArrayFromBoolean(object.getBoolValue()), 0, result, 80, 1);
        System.arraycopy(createByteArrayFromDateTime(object.getTimeValue()), 0, result, 81, 16);
        result[totalLength - 1] = (byte) '\n';
        return result;
    }

    // serialize long to byte array of 8
    public byte[] createByteArrayFromLong(Long number) {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        if (number != null) {
            buffer.putLong(number);
        }
        return buffer.array();
    }

    // convert a string to byte array of fixed size of 64 and filles rest with 0
    // bytes
    public byte[] createByteArrayFromString(String string) {
        byte[] result = new byte[64];
        if (string != null) {
            byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
            System.arraycopy(bytes, 0, result, 0, Math.min(bytes.length, 64));
        }
        return result;
    }

    // convert boolean to byte array of lenght 1
    public byte[] createByteArrayFromBoolean(Boolean flag) {
        byte[] result = new byte[1];
        if (flag != null) {
            result[0] = (byte) (flag ? 1 : 0);
        }
        return result;
    }

    // serializes time to binary according to GO serializing format, curently it
    // converts all time to UTC and saves it in UTC timezone
    public byte[] createByteArrayFromDateTime(ZonedDateTime date) throws Exception {
        byte[] byteArray = new byte[16];

        if (date == null) {
            return byteArray;
        }
        Instant unixSec = date.toInstant();
        long offset_min = 0;
        long offset_sec = 0;
        int version = 1;

        if (date.getZone().getId().equals("Z")) {
            offset_min = -1;
        } else {
            ZoneOffset offset = date.getOffset();
            Duration offsetDuration = Duration.ofSeconds(offset.getTotalSeconds());

            if (offsetDuration.getSeconds() % 60 != 0) {
                version = 2;
                offset_sec = offsetDuration.getSeconds() % 60;
            }

            offset_min = offsetDuration.toMinutes();
            if (offset_min < -32768 || offset_min == -1 || offset_min > 32767) {
                throw new Exception("unexpected zone offset");
            }
        }
        Long goSec = unixSec.getEpochSecond() + GO_MAGIC_UNIX_TO_INTERNAL;
        int nano = unixSec.getNano();

        byteArray[0] = (byte) version;
        byteArray[1] = (byte) ((goSec >> 56) & 0xFF);
        byteArray[2] = (byte) ((goSec >> 48) & 0xFF);
        byteArray[3] = (byte) ((goSec >> 40) & 0xFF);
        byteArray[4] = (byte) ((goSec >> 32) & 0xFF);
        byteArray[5] = (byte) ((goSec >> 24) & 0xFF);
        byteArray[6] = (byte) ((goSec >> 16) & 0xFF);
        byteArray[7] = (byte) ((goSec >> 8) & 0xFF);
        byteArray[8] = (byte) ((goSec >> 0) & 0xFF);
        byteArray[9] = (byte) ((nano >> 24) & 0xFF);
        byteArray[10] = (byte) ((nano >> 16) & 0xFF);
        byteArray[11] = (byte) ((nano >> 8) & 0xFF);
        byteArray[12] = (byte) ((nano >> 0) & 0xFF);
        byteArray[13] = (byte) ((offset_min >> 8) & 0xFF);
        byteArray[14] = (byte) ((offset_min >> 0) & 0xFF);
        if (version == 2) {
            byteArray[15] = (byte) ((offset_sec) & 0xFF);
        }

        return byteArray;
    }
}
