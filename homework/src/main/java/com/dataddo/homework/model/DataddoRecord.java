package com.dataddo.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;

public class DataddoRecord {

    @JsonProperty("ID")
    private Long iD;

    @JsonProperty("IntValue")
    private Long intValue;

    @JsonProperty("StrValue")
    private String strValue;

    @JsonProperty("BoolValue")
    private Boolean boolValue;

    @JsonProperty("TimeValue")
    private ZonedDateTime timeValue;

    // create with default value 0
    public DataddoRecord() {
        this.iD = Long.parseLong("0");
    }

    @JsonIgnore
    public Long getID() {
        return iD;
    }

    @JsonIgnore
    public void setID(Long iD) {
        this.iD = iD;
    }

    public Long getIntValue() {
        return intValue;
    }

    public void setIntValue(Long intValue) {
        this.intValue = intValue;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public void setBoolValue(Boolean boolValue) {
        this.boolValue = boolValue;
    }

    public ZonedDateTime getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(ZonedDateTime timeValue) {
        this.timeValue = timeValue;
    }
}
