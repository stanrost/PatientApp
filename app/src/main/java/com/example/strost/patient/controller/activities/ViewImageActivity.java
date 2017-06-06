package com.example.strost.patient.controller.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.strost.patient.R;
import com.example.strost.patient.model.entities.Exercise;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.RelativeLayout.*;

/**
 * Created by strost on 30-3-2017.
 */

public class ViewImageActivity extends AppCompatActivity {
    private ImageView mFullScreenImageView;
    private final String PICTURE_KEY = "picture";
    private final String OFFLINE_PICTURE_KEY = "offlinePicture";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        final String picture = getIntent().getStringExtra(PICTURE_KEY);
        String mOfflineUrl = "";
        mOfflineUrl = getIntent().getStringExtra(OFFLINE_PICTURE_KEY);
        mFullScreenImageView = (ImageView) findViewById(R.id.ivFullScreenImage);
        ImageView mCloseActivity = (ImageView) findViewById(R.id.ivClose);
        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.pbLoadingImage);
        mProgressBar.bringToFront();
        if(mOfflineUrl.equals("")) {
            mProgressBar.setVisibility(View.VISIBLE);
           Picasso.with(this).load(picture).fit().centerInside().into(mFullScreenImageView, new Callback() {
               @Override
               public void onSuccess() {
                   mProgressBar.setVisibility(View.GONE);
               }

               @Override
               public void onError() {
               }
           });

        }else{
            mFullScreenImageView.setImageURI(Uri.fromFile(new File(mOfflineUrl)));
        }

        PhotoViewAttacher mAttacher;
        mAttacher = new PhotoViewAttacher(mFullScreenImageView);
        mAttacher.update();

        mCloseActivity.bringToFront();
        mCloseActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFullImage();
            }
        });
    }

    public void closeFullImage(){
        finish();
    }
}
