package com.example.strost.patient.model.request;

import com.backendless.Backendless;
import com.backendless.services.messaging.MessageStatus;
import com.example.strost.patient.model.entities.Exercise;
import com.example.strost.patient.model.entities.Patient;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.strost.patient.application.ApplicationEx.restAdapter;

/**
 * Created by strost on 24-3-2017.
 */

public class PutPatientRequest {

    public void updatePatient(final Patient p) {
        Runnable runnable = new Runnable() {
            public void run() {
               Backendless.Persistence.save(p);
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
}
