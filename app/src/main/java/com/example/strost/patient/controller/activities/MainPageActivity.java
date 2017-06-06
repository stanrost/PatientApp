package com.example.strost.patient.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.strost.patient.R;
import com.example.strost.patient.controller.adapters.PatientDataAdapter;
import com.example.strost.patient.model.entities.Exercise;
import com.example.strost.patient.model.entities.Patient;
import com.example.strost.patient.model.request.GetPatientRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by strost on 22-3-2017.
 */

public class MainPageActivity extends AppCompatActivity {

    private Patient mPatient;
    private List<Exercise> mExercises = new ArrayList<Exercise>();
    private final String PATIENT_KEY = "Patient";

    private PatientDataAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        mPatient = (Patient) getIntent().getSerializableExtra(PATIENT_KEY);
        GetPatientRequest gPR = new GetPatientRequest();
        mPatient = gPR.getPatients(mPatient);
        mExercises = mPatient.getExercises();

        Collections.sort(mExercises, new Comparator<Exercise>() {
            @Override
            public int compare(Exercise exercise1, Exercise exercise2) {
                int id1 = exercise1.getId();
                int id2 = exercise2.getId();
                return id2 > id1 ? +1 : id2 < id1 ? -1 : 0;
            }
        });
        initRecyclerView();
        setExerciseList();

    }

    public void goToSettingsActvivity(){
        Intent detailIntent = new Intent(this, SettingsActivity.class);
        detailIntent.putExtra(PATIENT_KEY, mPatient);
        startActivity(detailIntent);
        finish();
    }

    public void refreshList(){
        GetPatientRequest gPR = new GetPatientRequest();
        mPatient = gPR.getPatients(mPatient);
        mExercises.clear();
        List<Exercise> reloadedExercises= mPatient.getExercises();
        Collections.sort(reloadedExercises, new Comparator<Exercise>() {
            @Override
            public int compare(Exercise exercise1, Exercise exercise2)
            {
                int id1 = exercise1.getId();
                int id2 = exercise2.getId();
                return id2 > id1 ? +1 : id2 < id1 ? -1 : 0;
            }
        });

        mExercises.addAll(reloadedExercises);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_patient, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.settings) {
            goToSettingsActvivity();
        }
        else if (id == R.id.menu_refresh) {
            refreshList();
        }
        return super.onOptionsItemSelected(item);
    }
    public void setExerciseList() {

        mAdapter = new PatientDataAdapter(mExercises, mPatient);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rvExercises);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
    }


}
