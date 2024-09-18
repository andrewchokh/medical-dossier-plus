package com.andrewchokh.medicaldossierplus.Types;

import java.util.Date;

public class Record {
    private final int id;
    private final int regularUserId;
    private final int adminUserId;
    private final String content;
    private final Date creationDate;
    private final float weight;
    private final int pulse;
    private final int systolicBloodPressure;
    private final int diastolicBloodPressure;
    private final float bodyTemperature;

    public Record(int id, int regularUserId, int adminUserId, String content, Date creationDate,
        float weight, int pulse, int systolicBloodPressure, int diastolicBloodPressure,
        float bodyTemperature) {
        this.id = id;
        this.regularUserId = regularUserId;
        this.adminUserId = adminUserId;
        this.content = content;
        this.creationDate = creationDate;
        this.weight = weight;
        this.pulse = pulse;
        this.systolicBloodPressure = systolicBloodPressure;
        this.diastolicBloodPressure = diastolicBloodPressure;
        this.bodyTemperature = bodyTemperature;
    }

    public int getId() {
        return id;
    }

    public int getRegularUserId() {
        return regularUserId;
    }

    public int getAdminUserId() {
        return adminUserId;
    }

    public String getContent() {
        return content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public float getWeight() {
        return weight;
    }

    public int getPulse() {
        return pulse;
    }

    public int getSystolicBloodPressure() {
        return systolicBloodPressure;
    }

    public int getDiastolicBloodPressure() {
        return diastolicBloodPressure;
    }

    public float getBodyTemperature() {
        return bodyTemperature;
    }
}
