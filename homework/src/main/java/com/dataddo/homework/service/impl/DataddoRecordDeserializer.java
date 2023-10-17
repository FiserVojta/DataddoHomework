package com.dataddo.homework.service.impl;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;

import com.dataddo.homework.model.DataddoRecord;

@Component
public class DataddoRecordDeserializer {

    // difference from standart epoch to GO epoch
    private final Long SECONDS_PER_DAY = 86400L;
    private final Long GO_MAGIC_INTERNAL_TO_UNIX = (1969L * 365 + 1969 / 4 - 1969 / 100 + 1969 / 400) * SECONDS_PER_DAY;

    // creates a dataddo record from byte array of lenght 98

    public DataddoRecord createDataRecordFromByteArray(byte[] result) throws UnsupportedEncodingException {
        DataddoRecord record = new DataddoRecord();

        byte[] Idbytes = new byte[8];
        System.arraycopy(result, 0, Idbytes, 0, 8);
        record.setID(desereliazeLong(Idbytes));
        if (record.getID() == 0) {
            return record;
        }

        byte[] intValueBytes = new byte[8];
        System.arraycopy(result, 8, intValueBytes, 0, 8);
        record.setIntValue(desereliazeLong(intValueBytes));

        byte[] strValueBytes = new byte[64];
        System.arraycopy(result, 16, strValueBytes, 0, 64);
        record.setStrValue(desereliazeString(strValueBytes).trim());

        record.setBoolValue(desereliazeBoolean(result[80]));

        byte[] dateTimeBytes = new byte[16];
        System.arraycopy(result, 81, dateTimeBytes, 0, 16);
        record.setTimeValue(desereliazeDateTime(dateTimeBytes));

        return record;
    }

    public Long desereliazeLong(byte[] array) {
        ByteBuffer buffer = ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getLong();
    }

    public String desereliazeString(byte[] array) throws UnsupportedEncodingException {
        String result;
        result = new String(array, "UTF-8");
        return result;
    }

    public boolean desereliazeBoolean(byte b) {
        return b != 0;
    }

    // custom implementation of deseralization of go time.
    public ZonedDateTime desereliazeDateTime(byte[] data) {
        if (data.length == 0) {
            throw new IllegalArgumentException("no data");
        }
        byte version = data[0];
        if (version != 1 && version != 2) {
            throw new IllegalArgumentException("unsupported version");
        }

        ByteBuffer buf = ByteBuffer.wrap(data, 1, data.length - 1);

        long sec = buf.getLong();
        int nsec = buf.getInt();
        short offsetMin = buf.getShort();
        int offsetSec = offsetMin * 60;

        if (version == 2) {
            offsetSec += Byte.toUnsignedInt(buf.get());
        }

        Instant instant = Instant.ofEpochSecond(sec - GO_MAGIC_INTERNAL_TO_UNIX, nsec);
        ZonedDateTime dt = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);

        if (offsetSec == -1 * 60) {
            return dt.withZoneSameInstant(ZoneOffset.UTC);
        } else {
            ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetSec);
            return dt.withZoneSameInstant(offset);
        }
    }

}
