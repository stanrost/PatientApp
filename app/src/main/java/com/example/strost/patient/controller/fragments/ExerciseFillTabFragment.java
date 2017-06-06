package com.example.strost.patient.controller.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.strost.patient.R;
import com.example.strost.patient.controller.activities.ExerciseActivity;
import com.example.strost.patient.controller.activities.ViewImageActivity;
import com.example.strost.patient.model.entities.Exercise;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by strost on 23-3-2017.
 */

public class ExerciseFillTabFragment extends Fragment {

    private RadioGroup mRating;
    private Exercise mExercise;
    private Button mTakePictureButton;
    private ImageView mImageView;
    private Switch mHelpSwitch;
    private EditText mNotesEditText;
    private final String EXERCISE_KEY = "Exercise";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_fillexersicetab, container, false);

        mHelpSwitch = (Switch) rootView.findViewById(R.id.swtchGotHelp);
        mNotesEditText = (EditText) rootView.findViewById(R.id.etCaregiverNote);

        mTakePictureButton = (Button) rootView.findViewById(R.id.btnTakePicture);
        mImageView = (ImageView) rootView.findViewById(R.id.ivPreviewImage);
        Button stop  = (Button) rootView.findViewById(R.id.btnStop);
        Button play  = (Button) rootView.findViewById(R.id.btnPlay);
        Button start  = (Button) rootView.findViewById(R.id.btnStart);

        mRating = (RadioGroup) rootView.findViewById(R.id.rgRating);
        FloatingActionButton fabAddFeedback = (FloatingActionButton) rootView.findViewById(R.id.fabAddFeedback);

        final NestedScrollView nsv = (NestedScrollView) rootView.findViewById(R.id.scrollview);
        nsv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                nsv.clearFocus();
                return false;
            }
        });

        fabAddFeedback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = getIndexRating();
                ((ExerciseActivity)getActivity()).savePatient(i, mHelpSwitch.isChecked(), mNotesEditText.getText().toString());
            }
        });

        mTakePictureButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExerciseActivity)getActivity()).takePicture(mImageView);
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotofullimage(rootView);
            }
        });


        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExerciseActivity)getActivity()).audioPlayer();
            }
        });

        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExerciseActivity)getActivity()).startRecord();
            }
        });

        stop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExerciseActivity)getActivity()).stopRecord();
            }
        });
        return rootView;
    }

    public void gotofullimage(View rootview){

        Intent detailIntent = new Intent(rootview.getContext(), ViewImageActivity.class);
        detailIntent.putExtra(EXERCISE_KEY, mExercise);
        detailIntent.putExtra("picture", "");
        detailIntent.putExtra("offlinePicture", ((ExerciseActivity) getActivity()).getUrl());
        startActivity(detailIntent);
    }

    public void setExercise(Exercise exercise) {
        mExercise = exercise;
    }

    public int getIndexRating() {
        int rgid = mRating.getCheckedRadioButtonId();

        int index = 0;
        if (rgid == R.id.rbReallyHappy) {
            index = 1;
        }

        if (rgid == R.id.rbHappy) {
            index = 2;
        }

        if (rgid == R.id.rbNatural) {
            index = 3;
        }

        if (rgid == R.id.rbSad) {
            index = 4;
        }

        if (rgid == R.id.rbReallySad) {
            index = 5;
        }

        return index;
    }



}
