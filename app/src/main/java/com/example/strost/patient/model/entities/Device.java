package com.example.strost.patient.model.entities;

import java.io.Serializable;


public class Device implements Serializable {
    private String objectId;
    private String deviceId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


}
