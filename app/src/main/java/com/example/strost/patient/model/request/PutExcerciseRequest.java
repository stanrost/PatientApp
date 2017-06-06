package com.example.strost.patient.model.request;

import android.util.Log;

import com.backendless.Backendless;
import com.example.strost.patient.model.entities.Exercise;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.strost.patient.application.ApplicationEx.restAdapter;

/**
 * Created by strost on 24-3-2017.
 */

public class PutExcerciseRequest {

    public void updateExercise(final Exercise ex) {

        Runnable runnable = new Runnable() {
            public void run() {
                Backendless.Persistence.save(ex);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       /* CompositeDisposable mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(restAdapter.putRequest(ex.getObjectId(), ex)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Exercise>() {
                    @Override
                    public void onNext(Exercise value) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("dit gaat fout", "gAAAT FOUTTT");
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
*/

    }
}
