package com.example.strost.patient.controller.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.strost.patient.R;
import com.example.strost.patient.controller.fragments.ExerciseFeedbackTabFragment;
import com.example.strost.patient.controller.fragments.ExerciseFillTabFragment;
import com.example.strost.patient.controller.fragments.ExerciseTabFragment;
import com.example.strost.patient.model.entities.Exercise;
import com.example.strost.patient.model.entities.Feedback;
import com.example.strost.patient.model.entities.Patient;
import com.example.strost.patient.model.request.AddPictureRequest;
import com.example.strost.patient.model.request.AddRecordRequest;
import com.example.strost.patient.model.request.PutExcerciseRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by strost on 22-3-2017.
 */

public class ExerciseActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Exercise mExercise;
    private Patient mPatient;
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder mRrecorder = null;
    private int currentFormat = 0;
    private int output_formats[] = {MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP};

    private String mAudioFilename;
    private File mAudioFile = null;
    private Uri mPictureFile = null;
    private String mPictureLocation;
    private ImageView mImageView;
    private String mOriginalAdioFileName;

    private final String PATIENT_KEY = "Patient";
    private final String EXERCISE_KEY = "Exercise";

    private final String AUDIOFILE_URL = "https://api.backendless.com/e5a95319-dfee-9344-ff32-50448355ec00/v1/files/media/recorded/";
    private final String PICTURE_URL = "https://api.backendless.com/e5a95319-dfee-9344-ff32-50448355ec00/v1/files/media/";

    private final String AUDIOFILE_TYPE = ".wma";
    private final String PICTURE_TYPE = ".png";

    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    private final static String CAPTURED_PHOTO_URI_KEY = "mCapturedImageURI";

    private String mCurrentPhotoPath = null;
    private Uri mCapturedImageURI = null;

    private int imageRotation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mExercise = (Exercise) getIntent().getSerializableExtra(EXERCISE_KEY);
        mPatient = (Patient) getIntent().getSerializableExtra(PATIENT_KEY);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 0);
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    ExerciseTabFragment tab1 = new ExerciseTabFragment();
                    tab1.setExercise(mExercise);
                    return tab1;
                case 1:
                    ExerciseFillTabFragment tab2 = new ExerciseFillTabFragment();
                    tab2.setExercise(mExercise);
                    return tab2;
                case 2:
                    ExerciseFeedbackTabFragment tab3 = new ExerciseFeedbackTabFragment();
                    tab3.setExercise(mExercise);
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.exercise);
                case 1:
                    return getString(R.string.fill_exercise);
                case 2:
                    return getString(R.string.filled_in_exercises);
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
        return;
    }

    public void goBack() {
        Intent detailIntent = new Intent(this, MainPageActivity.class);
        detailIntent.putExtra(PATIENT_KEY, mPatient);
        startActivity(detailIntent);
        finish();
    }

// take picture

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mCurrentPhotoPath != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath);
        }
        if (mCapturedImageURI != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_URI_KEY, mCapturedImageURI.toString());
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
        }
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_URI_KEY)) {
            mCapturedImageURI = Uri.parse(savedInstanceState.getString(CAPTURED_PHOTO_URI_KEY));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    //save patient
    public void savePatient(int rating, boolean help, String notes) {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        String feedbackDate = sdf.format(c.getTime());


        Feedback feedback = new Feedback();
        feedback.setPatientRating(rating);
        feedback.setPatientHelp(help);
        feedback.setPatientNotes(notes);
        feedback.setFeedbackDate(feedbackDate);


        if (mPictureFile != null) {
            feedback.setPatientPicture(PICTURE_URL + mPictureLocation + PICTURE_TYPE);
            try {
                addPicture();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mAudioFile != null) {
            feedback.setPatientRecord(AUDIOFILE_URL + mOriginalAdioFileName + AUDIOFILE_TYPE);
            addRecord();
        }

        List feedbackList = mExercise.getFeedback();

        feedbackList.add(feedback);

        mExercise.setFeedback(feedbackList);

        PutExcerciseRequest ex = new PutExcerciseRequest();
        ex.updateExercise(mExercise);

        Toast.makeText(ExerciseActivity.this, getString(R.string.feedback_saved), Toast.LENGTH_LONG).show();
        //Temporary Solution
        mViewPager.setCurrentItem(0);
        mViewPager.setCurrentItem(1);

    }

    public void addRecord() {
        final AddRecordRequest aRR = new AddRecordRequest();
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    aRR.AddRecord(mAudioFilename, "media/recorded");
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

    }

    public void addPicture() throws IOException {
        Bitmap mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mPictureFile);

        //rotate
        Matrix matrix = new Matrix();
        matrix.postRotate(imageRotation);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_clear)
                        .setContentTitle(getString(R.string.upload_picture))
                        .setContentText(getString(R.string.notification_will_be_removed));


        final NotificationManager mNotifyMgr = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(001, mBuilder.build());

        final AddPictureRequest apr = new AddPictureRequest();
        final Bitmap finalMBitmap = mBitmap;
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    apr.AddPicture(finalMBitmap, mPictureLocation, mNotifyMgr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();

    }

    //take picture
    File photo = null;

    public void takePicture(ImageView image) {
        mImageView = image;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mPictureLocation = "Response_" + mExercise.getTitle().replace(" ", "") + "-" + timeStamp;
        photo = new File(Environment.getExternalStorageDirectory(), mPictureLocation);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mPictureFile = FileProvider.getUriForFile(
                this,
                this.getApplicationContext()
                        .getPackageName() + ".provider", photo);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPictureFile);

        startActivityForResult(intent, 100);
    }

    public String getUrl() {
        String photoUrl = "";
        try {
            photoUrl = photo.getAbsolutePath();
        } catch (NullPointerException e) {
        }
        return photoUrl;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                mImageView.setImageURI(mPictureFile);

                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(photo.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                exif.getAttribute(ExifInterface.TAG_ORIENTATION);

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                switch (orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        mImageView.setRotation(90);
                        imageRotation = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        mImageView.setRotation(180);
                        imageRotation = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        mImageView.setRotation(270);
                        imageRotation = 270;
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:

                    default:
                        break;
                }

            }
        }
    }

    // mRrecorder
    private void enableButton(int id, boolean isEnable) {
        ((Button) findViewById(id)).setEnabled(isEnable);
    }

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart, !isRecording);
        enableButton(R.id.btnStop, isRecording);
    }

    private String getmAudioFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        mAudioFile = file;

        if (!file.exists()) {
            file.mkdirs();
        }

        mOriginalAdioFileName = System.currentTimeMillis() + "";
        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIOFILE_TYPE);
    }

    private void startRecording() {
        mRrecorder = new MediaRecorder();
        mRrecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRrecorder.setOutputFormat(output_formats[currentFormat]);
        mRrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mAudioFilename = getmAudioFilename();
        mRrecorder.setOutputFile(mAudioFilename);
        mRrecorder.setOnErrorListener(errorListener);
        mRrecorder.setOnInfoListener(infoListener);
        try {
            mRrecorder.prepare();
            mRrecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (null != mRrecorder) {
            mRrecorder.stop();
            mRrecorder.reset();
            mRrecorder.release();
            mRrecorder = null;
        }
    }


    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
        }
    };

    public void startRecord() {

        Toast.makeText(ExerciseActivity.this, getString(R.string.start_recording), Toast.LENGTH_SHORT).show();
        enableButtons(true);
        startRecording();

    }

    public void stopRecord() {

        Toast.makeText(ExerciseActivity.this, getString(R.string.stop_recording), Toast.LENGTH_SHORT).show();
        enableButtons(false);
        stopRecording();
    }

    public void audioPlayer() {
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(mAudioFilename);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void playAudio() {
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(mExercise.getRecord());
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
