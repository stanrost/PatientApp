package com.example.strost.patient.model.request;


import android.app.NotificationManager;
import android.graphics.Bitmap;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

public class AddPictureRequest {

    private final String PICTURE_TYPE =".png";

    public void AddPicture(Bitmap file, String title, final NotificationManager mNotifyMgr) throws Exception {

        Backendless.Files.Android.upload(file,
                Bitmap.CompressFormat.PNG,
                100,
                title + PICTURE_TYPE,
                "media",
                new AsyncCallback<BackendlessFile>() {
                    @Override
                    public void handleResponse(BackendlessFile response) {
                        mNotifyMgr.cancel(001);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                    }
                });
    }

}
