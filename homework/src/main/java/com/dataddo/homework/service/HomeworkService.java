package com.dataddo.homework.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataddo.homework.handler.DataddoFileHandler;
import com.dataddo.homework.model.DataddoRecord;
import com.dataddo.homework.service.impl.DataddoRecordDeserializer;
import com.dataddo.homework.service.impl.DataddoRecordSerializer;

@Service
public class HomeworkService {

    private final int RECORDLENGHT = 98;
    @Autowired
    private DataddoFileHandler dataddoFileHandler;

    @Autowired
    private DataddoRecordSerializer dataddoRecordSerializer;

    @Autowired
    private DataddoRecordDeserializer dataddoRecordDeserializer;

    // get a position of the record of the record in the file and fetch it based on
    // that
    public DataddoRecord getRecordById(Long id) throws IOException, IllegalArgumentException {
        validateRecordInSize(id);
        long position = (id - 1) * RECORDLENGHT;
        byte[] data = dataddoFileHandler.readRecordFromFile(position, RECORDLENGHT);
        return dataddoRecordDeserializer.createDataRecordFromByteArray(data);
    }

    public DataddoRecord createReDataddoRecord(DataddoRecord record) throws Exception {
        validateDataddoRecord(record);
        writeDataddoRecordToFile(true, record, null);
        return record;

    }

    // validate serilize and updateRecord in file
    public DataddoRecord updateRecord(DataddoRecord record, Long id) throws Exception {
        validateRecordInSize(id);
        validateDataddoRecord(record);
        record.setID(id);
        this.writeDataddoRecordToFile(false, record, id);
        return record;
    }

    public DataddoRecord deleteRecord(Long id) throws Exception {
        validateRecordInSize(id);
        DataddoRecord record = new DataddoRecord();
        record.setID(Long.valueOf(0));
        this.writeDataddoRecordToFile(false, record, id);
        return record;

    }

    // synchronized method to prepare and then write data to file
    public synchronized DataddoRecord writeDataddoRecordToFile(Boolean appendRecordToEnd, DataddoRecord record,
            Long id)
            throws Exception {
        if (appendRecordToEnd) {
            record.setID(dataddoFileHandler.getFileLength() / RECORDLENGHT + 1);
            id = record.getID();
        }
        byte[] byteRecord = dataddoRecordSerializer.serializeDataddoRecord(record);
        long position = (id - 1) * RECORDLENGHT;
        dataddoFileHandler.writeRecordToFile(position, byteRecord);
        return record;
    }

    // method to validate request, so fat only string lenght is controlled
    public Boolean validateDataddoRecord(DataddoRecord record) throws IllegalArgumentException {
        if (record.getStrValue().length() >= 64) {
            throw new IllegalArgumentException("The String value is too long");
        }
        return true;
    }

    // validate if the record is in size of file
    public Boolean validateRecordInSize(Long Id) throws IOException {
        if ((Id - 1) * RECORDLENGHT >= dataddoFileHandler.getFileLength()) {
            throw new IllegalArgumentException("Requested data exceeds file size.");
        }
        return true;
    }

}
