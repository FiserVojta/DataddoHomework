package com.dataddo.homework.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataddo.homework.model.DataddoRecord;
import com.dataddo.homework.service.HomeworkService;

@RestController
@RequestMapping("")
public class HomeworkController {

    @Autowired
    HomeworkService homeworkService;

    @GetMapping("/readyz")
    public String healthProbe() {
        return "Ready";
    }

    @PostMapping("/records")
    public ResponseEntity<DataddoRecord> createRecord(@RequestBody DataddoRecord dataddoRecord) {
        try {
            DataddoRecord record = homeworkService.createReDataddoRecord(dataddoRecord);
            return ResponseEntity.status(HttpStatus.CREATED).body(record);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/records/{id}")
    public ResponseEntity<DataddoRecord> getRecordById(@PathVariable("id") Long id) {
        try {
            DataddoRecord record = homeworkService.getRecordById(id);
            if (record.getID() == 0) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(record);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @PutMapping("/records/{id}")
    public ResponseEntity<DataddoRecord> updateRecordById(@PathVariable("id") Long id,
            @RequestBody DataddoRecord dataddoRecord) {

        try {
            DataddoRecord record = homeworkService.updateRecord(dataddoRecord, id);
            if (record.getID() == 0) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(record);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @DeleteMapping("/records/{id}")
    public ResponseEntity deleteRecordById(@PathVariable("id") Long id) {
        try {
            homeworkService.deleteRecord(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

}
