package com.example.strost.patient.controller.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.strost.patient.R;
import com.example.strost.patient.controller.activities.ExerciseActivity;
import com.example.strost.patient.controller.activities.MainPageActivity;
import com.example.strost.patient.model.entities.Exercise;
import com.example.strost.patient.model.entities.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bram on 30/01/2017.
 */

public class PatientDataAdapter extends RecyclerView.Adapter<PatientDataAdapter.ViewHolder>{

    private ArrayList<Exercise> mExerciesList;
    Patient mPatient;
    private final String PATIENT_KEY = "Patient";
    private final String EXERCISE_KEY = "Exercise";

    public PatientDataAdapter(List<Exercise> androidList, Patient patient){
        mExerciesList = (ArrayList<Exercise>) androidList;
        mPatient = patient;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitle, mDescription;

        ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById( R.id.tvTitle);
            mDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mExerciesList.get(getPosition()).getTitle();
            goToPatientPage(v);
        }

        public void goToPatientPage(View v){
            Intent detailIntent = new Intent(v.getContext(), ExerciseActivity.class);
            detailIntent.putExtra(EXERCISE_KEY, mExerciesList.get(getPosition()));
            detailIntent.putExtra(PATIENT_KEY, mPatient);
            v.getContext().startActivity(detailIntent);
            ((MainPageActivity)v.getContext()).finish();

        }
    }


    @Override
    public PatientDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatientDataAdapter.ViewHolder holder, int position) {
        holder.mTitle.setText(mExerciesList.get(position).getTitle());
        holder.mDescription.setText(mExerciesList.get(position).getDescription().substring(0, Math.min(mExerciesList.get(position).getDescription().length(), 35))+"...");
    }

    @Override
    public int getItemCount() {
        return mExerciesList != null ? mExerciesList.size() : 0;
    }


}
