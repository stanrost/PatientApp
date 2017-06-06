package com.example.strost.patient.network;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.backendless.Backendless;
import com.example.strost.patient.controller.activities.LoginActivity;

import java.util.ArrayList;

/**
 * Created by strost on 30-3-2017.
 */

public class SendEmail {

    public void sendEmail(final String title, String email, final String mailBody){
        final ArrayList<String> patients = new ArrayList<String>();
        patients.add(email);
        Runnable runnable = new Runnable() {
            public void run() {
                Backendless.Messaging.sendHTMLEmail(title, mailBody, patients );

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
