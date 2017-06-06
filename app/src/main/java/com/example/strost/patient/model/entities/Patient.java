package com.example.strost.patient.model.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Patient implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("gender")
    private String gender;
    @SerializedName("problem")
    private String problem;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("objectId")
    private String objectId;
    @SerializedName("exercises")
    List<Exercise> exercises = new ArrayList<Exercise>();
    @SerializedName("devices")
    List<Device> devices = new ArrayList<Device>();
    @SerializedName("changedGeneratedPassword")
    private Boolean changedGeneratedPassword;
    @SerializedName("dataPolicyAccepted")
    private Boolean dataPolicyAccepted;

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void removeExcersice(Exercise exercise) {
        exercises.remove(exercise);
    }

    public void addExercise(int exerciseId, String exerciseTitle, String exercisePicture, Boolean help, String description) {
        Exercise o1 = new Exercise();
        o1.setId(exerciseId);
        o1.setTitle(exerciseTitle);
        o1.setPicture(exercisePicture);
        o1.setHelp(help);
        o1.setDescription(description);
        exercises.add(o1);
    }

    @Override
    public String toString() {
        return this.id + ". " + this.firstName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Boolean getChangedGeneratedPassword() {
        return changedGeneratedPassword;
    }

    public void setChangedGeneratedPassword(Boolean changedGeneratedPassword) {
        this.changedGeneratedPassword = changedGeneratedPassword;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public Boolean getDataPolicyAccepted() {
        return dataPolicyAccepted;
    }

    public void setDataPolicyAccepted(Boolean dataPolicyAccepted) {
        this.dataPolicyAccepted = dataPolicyAccepted;
    }
}