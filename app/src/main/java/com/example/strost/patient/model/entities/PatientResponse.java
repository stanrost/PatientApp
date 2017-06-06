package com.example.strost.patient.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PatientResponse {
    @SerializedName("data")
    private List<Patient> data;

    public PatientResponse(List<Patient> data) {
        this.data = data;
    }

    public List<Patient> getData() {
        return data;
    }

    public void setData(List<Patient> data) {
        this.data = data;
    }
}
