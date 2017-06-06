package com.example.strost.patient.controller.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.strost.patient.R;
import com.example.strost.patient.controller.adapters.FeedbackDataAdapter;
import com.example.strost.patient.model.entities.Exercise;
import com.example.strost.patient.model.entities.Feedback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by strost on 5-5-2017.
 */

public class ExerciseFeedbackTabFragment extends Fragment {


    Exercise mExercise;
    private List<Feedback> mFeedback = new ArrayList<Feedback>();
    private RecyclerView mRecyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_exercisefeedback, container, false);

        mFeedback = mExercise.getFeedback();
        Collections.sort(mFeedback, new Comparator<Feedback>() {
            @Override
            public int compare(Feedback feedback1, Feedback feedback2) {
                return feedback2.getFeedbackDate().compareToIgnoreCase(feedback1.getFeedbackDate());
            }
        });

        initRecyclerView(rootView);
        return rootView;
    }

    private void initRecyclerView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvFeedback);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        setFeedbackRecyclerView();
    }


    public void setFeedbackRecyclerView(){

        FeedbackDataAdapter mAdapter = new FeedbackDataAdapter(mExercise.getFeedback(), mExercise);
        mRecyclerView.setAdapter(mAdapter);
    }
    public void setExercise(Exercise exercise) {
        mExercise = exercise;
    }
}
