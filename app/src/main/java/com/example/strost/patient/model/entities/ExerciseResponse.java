package com.example.strost.patient.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ExerciseResponse {
    @SerializedName("data")
    private List<Exercise> data;

    public ExerciseResponse(List<Exercise> data) {
        this.data = data;
    }

    public List<Exercise> getData() {
        return data;
    }

    public void setData(List<Exercise> data) {
        this.data = data;
    }
}
