package com.example.strost.patient.model.request;

import com.example.strost.patient.model.entities.Patient;
import com.example.strost.patient.model.entities.PatientResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.example.strost.patient.application.ApplicationEx.restAdapter;

/**
 * Created by strost on 28-3-2017.
 */

public class GetPatientsRequest {
    private List<Patient> patients = new ArrayList<Patient>();

    public List<Patient> getPatients() {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    Call<PatientResponse> call = restAdapter.getPatients();
                    PatientResponse result = call.execute().body();
                    patients = result.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return patients;
    }
}
