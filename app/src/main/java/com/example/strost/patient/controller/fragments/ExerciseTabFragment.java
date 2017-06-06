package com.example.strost.patient.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.strost.patient.controller.activities.ExerciseActivity;
import com.example.strost.patient.network.NetworkImageView;
import com.example.strost.patient.R;
import com.example.strost.patient.controller.activities.ViewImageActivity;
import com.example.strost.patient.model.entities.Exercise;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by strost on 23-3-2017.
 */

public class ExerciseTabFragment extends Fragment {

    private Exercise mExercise;
    private EditText mSetId;
    private TextView mNeedHelp, mSetDate, mPictureText, mSetName, mDescription;
    private NetworkImageView mImageView;
    private Button mPlayAudio;

    private final String PICTURE_KEY = "picture";
    private final String OFFLINE_PICTURE_KEY = "offlinePicture";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_exercisetab, container, false);
        mPictureText = (TextView) rootView.findViewById(R.id.tvPicture);
        mNeedHelp = (TextView) rootView.findViewById(R.id.tvGetHelp);
        mSetId = (EditText) rootView.findViewById(R.id.etExerciseId);
        mSetDate = (TextView) rootView.findViewById(R.id.tvExerciseDate);
        mSetName = (TextView) rootView.findViewById(R.id.tvExerciseName);
        mDescription = (TextView) rootView.findViewById(R.id.tvCaregiverNote);
        mImageView = (NetworkImageView) rootView.findViewById(R.id.ivFullScreenImage);
        final ProgressBar progress = (ProgressBar) rootView.findViewById(R.id.pbLoadingImage);
        mPlayAudio = (Button) rootView.findViewById(R.id.btnPlayAudio);

        mSetId.setText(mExercise.getId() + "");
        mSetDate.setText(mExercise.getEndDate());
        mSetName.setText(mExercise.getTitle());
        mDescription.setText(mExercise.getDescription());
        mDescription.setMovementMethod(new ScrollingMovementMethod());
        showPicture(progress);
        showHelp();

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               gotofullimage(rootView);
            }
        });

        try{
            if(!mExercise.getRecord().equals(null)){
                mPlayAudio.setVisibility(View.VISIBLE);
            }
        }catch (NullPointerException e){

        }

        mPlayAudio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((ExerciseActivity)getActivity()).playAudio();
            }
        });

        return rootView;
    }

    private void showHelp() {
        boolean needHelp = false;
        try {
            needHelp = mExercise.getHelp();
        }catch (NullPointerException e){}

        if (needHelp == true) {
            mNeedHelp.setText(getString(R.string.yes));
        } else {
            mNeedHelp.setText(getString(R.string.no));
        }

    }

    private void showPicture(final ProgressBar progress) {
        try{
            if(!mExercise.getPicture().equals(null)) {
                progress.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(mExercise.getPicture()).into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        noPicture(progress);
                    }
                });
                mImageView.bringToFront();
            }
            else{
                noPicture(progress);
            }
        }catch (NullPointerException e){
            noPicture(progress);
        }
    }

    public void noPicture(ProgressBar progress){
        progress.setVisibility(View.GONE);
        mImageView.setVisibility(View.GONE);
        mPictureText.setVisibility(View.GONE);
    }

    public void gotofullimage(View rootview){
        Intent detailIntent = new Intent(rootview.getContext(), ViewImageActivity.class);
        detailIntent.putExtra(PICTURE_KEY, mExercise.getPicture());
        detailIntent.putExtra(OFFLINE_PICTURE_KEY, "");
        startActivity(detailIntent);
    }

    public void setExercise(Exercise exercise) {
        mExercise = exercise;
    }

}
