package com.example.strost.patient.model.request;

import com.example.strost.patient.model.entities.Patient;
import com.example.strost.patient.model.entities.PatientResponse;

import retrofit2.Call;

import static com.example.strost.patient.application.ApplicationEx.restAdapter;

/**
 * Created by strost on 3-4-2017.
 */

public class GetPatientRequest {

    public Patient getPatients(final Patient p) {
        final Patient[] result = new Patient[1];
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    Call<Patient> call = restAdapter.getPatient(p.getObjectId());
                    result[0] = call.execute().body();
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

        return result[0];
    }

}
