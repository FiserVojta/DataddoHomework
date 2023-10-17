package com.dataddo.homework.handler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

@Component
public class DataddoFileHandler {

    private final Lock fileWriteLock = new ReentrantLock();
    private final String fileName = "records.bin";

    public DataddoFileHandler() {
    }

    // curent lenght of the file
    public long getFileLength() {
        File file = new File(this.fileName);
        return file.length();
    }

    public String getFileName() {
        return fileName;
    }

    // writes byte array to a file based on position of the first byte with lock to
    // a file, so only 1 program can write at a moment to the file
    public void writeRecordToFile(long position, byte[] data) throws Exception {
        fileWriteLock.lock();
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            raf.seek(position);
            raf.write(data);
        } finally {
            fileWriteLock.unlock();
        }
    }

    public byte[] readRecordFromFile(long position, int recordLenght) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {

            raf.seek(position);
            byte[] data = new byte[recordLenght];
            raf.readFully(data);
            return data;
        }
    }

}
