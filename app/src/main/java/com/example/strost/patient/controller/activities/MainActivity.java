package com.example.strost.patient.controller.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.backendless.Backendless;
import com.example.strost.patient.R;
import com.example.strost.patient.SaveSharedPreference;
import com.example.strost.patient.model.entities.Patient;
import com.example.strost.patient.model.request.GetPatientsRequest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Patient> mPatients = new ArrayList<Patient>();
    private final String PATIENT_KEY = "Patient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String appVersion = "v1";
        Backendless.initApp( this, "E5A95319-DFEE-9344-FF32-50448355EC00", "FE40A368-BB4B-7B9C-FF31-240C9C1AE700", appVersion );
        getPatients();
    }

    public void getPatients(){

        GetPatientsRequest gPR = new GetPatientsRequest();
        mPatients = gPR.getPatients();
        toRightPage();

    }

    public void toRightPage(){

        if(SaveSharedPreference.getUserName(MainActivity.this).length() > 0){
            Intent detailIntent = new Intent(this, MainPageActivity.class);
            String id = SaveSharedPreference.getUserName(MainActivity.this);
            Patient p = null;
            for (int i = 0; i < mPatients.size(); i++) {
                if (mPatients.get(i).getObjectId().equals(id)){
                    p = mPatients.get(i);
                }
            }
            detailIntent.putExtra(PATIENT_KEY, p);
            startActivity(detailIntent);
            finish();
        }
        else{
            Intent detailIntent = new Intent(this, LoginActivity.class);
            startActivity(detailIntent);
            finish();
        }
    }



}
