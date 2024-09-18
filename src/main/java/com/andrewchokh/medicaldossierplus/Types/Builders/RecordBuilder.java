package com.andrewchokh.medicaldossierplus.Types.Builders;

import com.andrewchokh.medicaldossierplus.Types.Record;
import java.util.Date;

public class RecordBuilder {
    private int id;
    private int regularUserId;
    private int adminUserId;
    private String content;
    private Date creationDate;
    private float weight;
    private float pulse;
    private int systolicBloodPressure;
    private int diastolicBloodPressure;
    private float bodyTemperature;

    public RecordBuilder id(int id) {
        this.id = id;
        return this;
    }

    public RecordBuilder regularUserId(int regularUserId) {
        this.regularUserId = regularUserId;
        return this;
    }

    public RecordBuilder adminUserId(int adminUserId) {
        this.adminUserId = adminUserId;
        return this;
    }

    public RecordBuilder content(String content) {
        this.content = content;
        return this;
    }

    public RecordBuilder creationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public RecordBuilder weight(float weight) {
        this.weight = weight;
        return this;
    }

    public RecordBuilder pulse(float pulse) {
        this.pulse = pulse;
        return this;
    }

    public RecordBuilder systolicBloodPressure(int systolicBloodPressure) {
        this.systolicBloodPressure = systolicBloodPressure;
        return this;
    }

    public RecordBuilder diastolicBloodPressure(int diastolicBloodPressure) {
        this.diastolicBloodPressure = diastolicBloodPressure;
        return this;
    }

    public RecordBuilder bodyTemperature(float bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
        return this;
    }

    public Record build() {
        return new Record(
            id, regularUserId, adminUserId, content, creationDate, weight, pulse,
            systolicBloodPressure, diastolicBloodPressure, bodyTemperature
        );
    }
}
