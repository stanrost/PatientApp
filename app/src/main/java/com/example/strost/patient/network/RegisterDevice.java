package com.example.strost.patient.network;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by strost on 29-3-2017.
 */

public class RegisterDevice {

    public void registerDevice(){
        Backendless.Messaging.registerDevice("1009215672914", "default", new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
            }

            @Override
            public void handleFault(BackendlessFault fault) {
            }
        });
    }

}
